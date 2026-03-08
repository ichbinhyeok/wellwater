param(
    [string]$BaseUrl = "http://localhost:18080",
    [string]$OutputDir = "output/pdf",
    [int]$ReadyTimeoutSeconds = 90
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Find-PopplerBinary {
    param(
        [Parameter(Mandatory = $true)]
        [string]$BinaryName
    )

    $command = Get-Command $BinaryName -ErrorAction SilentlyContinue
    if ($command) {
        return $command.Source
    }

    $packageRoot = Join-Path $env:LOCALAPPDATA "Microsoft\\WinGet\\Packages"
    $match = Get-ChildItem $packageRoot -Recurse -Filter $BinaryName -ErrorAction SilentlyContinue |
        Select-Object -First 1 -ExpandProperty FullName
    if ($match) {
        return $match
    }

    throw "$BinaryName was not found. Install Poppler first."
}

function Wait-ForServer {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Url,
        [Parameter(Mandatory = $true)]
        [int]$TimeoutSeconds
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    while ((Get-Date) -lt $deadline) {
        try {
            $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 3
            if ($response.StatusCode -ge 200) {
                return
            }
        } catch {
        }
        Start-Sleep -Seconds 2
    }

    throw "Server did not become ready within $TimeoutSeconds seconds."
}

function Stop-BackgroundProcess {
    param(
        [Parameter(Mandatory = $false)]
        [System.Diagnostics.Process]$Process
    )

    if ($null -ne $Process -and -not $Process.HasExited) {
        Stop-Process -Id $Process.Id -Force -ErrorAction SilentlyContinue
    }
}

New-Item -ItemType Directory -Force -Path "build" | Out-Null
New-Item -ItemType Directory -Force -Path $OutputDir | Out-Null

$stdoutLog = "build/bootrun.log"
$stderrLog = "build/bootrun.err.log"
if (Test-Path $stdoutLog) { Remove-Item $stdoutLog -Force }
if (Test-Path $stderrLog) { Remove-Item $stderrLog -Force }

$process = $null

try {
    $process = Start-Process `
        -FilePath "cmd.exe" `
        -ArgumentList "/c gradlew.bat bootRun --args=""--server.port=18080"" --console=plain" `
        -WorkingDirectory (Get-Location).Path `
        -RedirectStandardOutput $stdoutLog `
        -RedirectStandardError $stderrLog `
        -PassThru

    Wait-ForServer -Url "$BaseUrl/" -TimeoutSeconds $ReadyTimeoutSeconds

    $response = Invoke-WebRequest `
        -Method Post `
        -Uri "$BaseUrl/tool/result" `
        -UseBasicParsing `
        -Body @{
            entryMode = "result-first"
            analyteName = "nitrate"
            resultValue = "12"
            unit = "mg/L"
            qualifier = "none"
            sampleDate = "2026-03-01"
            sampleSource = "raw well"
            labCertified = "yes"
            state = "IA"
            useScope = "drinking-only"
            infantPresent = "true"
        }

    $html = $response.Content
    $match = [regex]::Match($html, "/result/saved/([A-Za-z0-9-]+)")
    if (-not $match.Success) {
        throw "Saved result path was not found in the tool response."
    }

    $snapshotId = $match.Groups[1].Value
    $pdfPath = Join-Path $OutputDir "result-snapshot.pdf"
    Invoke-WebRequest -Uri "$BaseUrl/result/saved/$snapshotId.pdf" -OutFile $pdfPath -UseBasicParsing

    $pdftoppm = Find-PopplerBinary -BinaryName "pdftoppm.exe"
    & $pdftoppm -png $pdfPath (Join-Path $OutputDir "result-snapshot") | Out-Null

    Write-Output "snapshot_id=$snapshotId"
    Write-Output "pdf_path=$pdfPath"
    Get-ChildItem $OutputDir | Sort-Object Name | ForEach-Object {
        Write-Output ("artifact={0}|{1}" -f $_.FullName, $_.Length)
    }
} finally {
    Stop-BackgroundProcess -Process $process
}
