param(
    [string]$Path = "data/pseo/pages.csv",
    [int]$MinRows = 60
)

$ErrorActionPreference = "Stop"

if (-not (Test-Path -Path $Path -PathType Leaf)) {
    Write-Error "CSV file not found: $Path"
}

$rows = Import-Csv $Path

$requiredColumns = @(
    "family",
    "slug",
    "title",
    "h1",
    "meta_description",
    "intro",
    "action_now",
    "what_to_test",
    "primary_cta_label",
    "primary_cta_url",
    "money_cta_label",
    "money_cta_url",
    "disclosure",
    "source_url",
    "search_query",
    "search_performed_at",
    "fetched_at",
    "tier"
)

if ($rows.Count -eq 0) {
    Write-Error "CSV is empty: $Path"
}

$presentColumns = $rows[0].PSObject.Properties.Name
$missingColumns = @($requiredColumns | Where-Object { $_ -notin $presentColumns })
if ($missingColumns.Count -gt 0) {
    Write-Error ("Missing required columns: " + ($missingColumns -join ", "))
}

$errors = New-Object System.Collections.Generic.List[string]

if ($rows.Count -lt $MinRows) {
    $errors.Add("Expected at least $MinRows rows but found $($rows.Count)")
}

$allowedFamilies = @("contaminants", "symptoms", "compares", "triggers", "regional", "authority")
$dupeSlugs = $rows | Group-Object slug | Where-Object { $_.Count -gt 1 }
foreach ($d in $dupeSlugs) {
    $errors.Add("Duplicate slug: $($d.Name)")
}

function IsBlank([string]$value) {
    return [string]::IsNullOrWhiteSpace($value)
}

for ($i = 0; $i -lt $rows.Count; $i++) {
    $row = $rows[$i]
    $lineNo = $i + 2

    foreach ($column in $requiredColumns) {
        if (IsBlank $row.$column) {
            $errors.Add("Line $lineNo missing required value: $column")
        }
    }

    if ($row.family -notin $allowedFamilies) {
        $errors.Add("Line $lineNo has unsupported family: $($row.family)")
    }

    if (-not ($row.source_url -like "http://*" -or $row.source_url -like "https://*")) {
        $errors.Add("Line $lineNo has non-http source_url: $($row.source_url)")
    }

    $searchAt = [datetime]::MinValue
    $fetchedAt = [datetime]::MinValue
    $searchOk = [datetime]::TryParse($row.search_performed_at, [ref]$searchAt)
    $fetchedOk = [datetime]::TryParse($row.fetched_at, [ref]$fetchedAt)

    if (-not $searchOk) {
        $errors.Add("Line $lineNo has invalid search_performed_at: $($row.search_performed_at)")
    }
    if (-not $fetchedOk) {
        $errors.Add("Line $lineNo has invalid fetched_at: $($row.fetched_at)")
    }
    if ($searchOk -and $fetchedOk -and $fetchedAt -lt $searchAt) {
        $errors.Add("Line $lineNo has fetched_at earlier than search_performed_at")
    }
}

if ($errors.Count -gt 0) {
    Write-Host "Validation failed with $($errors.Count) issue(s):"
    foreach ($err in $errors) {
        Write-Host "- $err"
    }
    exit 1
}

Write-Host "Validation passed"
Write-Host "Rows: $($rows.Count)"
Write-Host "Family counts:"
$rows |
    Group-Object family |
    Sort-Object Name |
    ForEach-Object { Write-Host ("- {0}: {1}" -f $_.Name, $_.Count) }
