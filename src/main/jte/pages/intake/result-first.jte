@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> analytes

@template.layout.main(title = "Result-First Well Water Tool", robots = "index,follow", description = "Enter test results and get a structured decision output.", content = @`
    <section class="panel">
        <h1>Result-First Decision Entry</h1>
        <p class="muted">Paste your current result context. The tool will output tier, multi-axis verdict, and next actions.</p>
    </section>

    <section class="panel">
        <form method="post" action="/tool/result">
            <input type="hidden" name="entryMode" value="result-first">
            <input type="hidden" name="slugHint" value="${request.getSlugHint()}">

            <label>Analyte</label>
            <select name="analyteName">
                <option value="">Select analyte</option>
                @for (var analyte : analytes)
                    <option value="${analyte}" @if (analyte.equalsIgnoreCase(request.getAnalyteName()))selected@end>${analyte}</option>
                @endfor
            </select>

            <label>Result Value</label>
            <input name="resultValue" value="${request.getResultValue()}" placeholder="e.g. 12 mg/L or positive">

            <label>Unit</label>
            <select name="unit">
                <option value="">Select unit</option>
                <option value="mg/L">mg/L</option>
                <option value="ppb">ppb</option>
                <option value="cfu">CFU</option>
                <option value="unknown">unknown</option>
            </select>

            <label>Sample Source</label>
            <select name="sampleSource">
                <option value="raw well">raw well</option>
                <option value="treated tap">treated tap</option>
                <option value="unknown">unknown</option>
            </select>

            <label>Lab Certified</label>
            <select name="labCertified">
                <option value="yes">yes</option>
                <option value="no">no</option>
                <option value="unknown">unknown</option>
            </select>

            <div class="panel" style="margin-top: 12px;">
                <p class="small muted">Vulnerable household flags</p>
                <label><input type="checkbox" name="infantPresent" value="true"> infant present</label>
                <label><input type="checkbox" name="pregnancyPresent" value="true"> pregnancy present</label>
                <label><input type="checkbox" name="immunocompromisedPresent" value="true"> immunocompromised present</label>
            </div>

            <button type="submit">Run Decision Engine</button>
        </form>
    </section>
`)

