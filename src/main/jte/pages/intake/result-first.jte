@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> analytes
@param java.util.List<String> qualifiers
@param java.util.List<String> states
@param java.util.List<String> useScopes
@param java.util.List<String> existingTreatments

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
                <option value="mg/L" @if ("mg/L".equalsIgnoreCase(request.getUnit()))selected@end>mg/L</option>
                <option value="ppm" @if ("ppm".equalsIgnoreCase(request.getUnit()))selected@end>ppm</option>
                <option value="ppb" @if ("ppb".equalsIgnoreCase(request.getUnit()))selected@end>ppb</option>
                <option value="ug/L" @if ("ug/L".equalsIgnoreCase(request.getUnit()))selected@end>ug/L</option>
                <option value="ppt" @if ("ppt".equalsIgnoreCase(request.getUnit()))selected@end>ppt</option>
                <option value="pCi/L" @if ("pCi/L".equalsIgnoreCase(request.getUnit()))selected@end>pCi/L</option>
                <option value="grains/gal" @if ("grains/gal".equalsIgnoreCase(request.getUnit()))selected@end>grains/gal</option>
                <option value="su" @if ("su".equalsIgnoreCase(request.getUnit()))selected@end>su</option>
                <option value="cfu/100ml" @if ("cfu/100ml".equalsIgnoreCase(request.getUnit()))selected@end>CFU/100mL</option>
                <option value="presence/absence" @if ("presence/absence".equalsIgnoreCase(request.getUnit()))selected@end>presence/absence</option>
                <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getUnit()))selected@end>unknown</option>
            </select>

            <label>Qualifier</label>
            <select name="qualifier">
                @for (var qualifier : qualifiers)
                    <option value="${qualifier}" @if (qualifier.equalsIgnoreCase(request.getQualifier()))selected@end>${qualifier}</option>
                @endfor
            </select>

            <label>Qualifier Value (optional)</label>
            <input name="qualifierValue" value="${request.getQualifierValue()}" placeholder="e.g. 0.005">

            <label>Sample Date</label>
            <input type="date" name="sampleDate" value="${request.getSampleDate()}">

            <label>Sample Source</label>
            <select name="sampleSource">
                <option value="raw well" @if ("raw well".equalsIgnoreCase(request.getSampleSource()))selected@end>raw well</option>
                <option value="treated tap" @if ("treated tap".equalsIgnoreCase(request.getSampleSource()))selected@end>treated tap</option>
                <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getSampleSource()))selected@end>unknown</option>
            </select>

            <label>Lab Certified</label>
            <select name="labCertified">
                <option value="yes" @if ("yes".equalsIgnoreCase(request.getLabCertified()))selected@end>yes</option>
                <option value="no" @if ("no".equalsIgnoreCase(request.getLabCertified()))selected@end>no</option>
                <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getLabCertified()))selected@end>unknown</option>
            </select>

            <label>State</label>
            <select name="state">
                <option value="">Select state</option>
                @for (var state : states)
                    <option value="${state}" @if (state.equalsIgnoreCase(request.getState()))selected@end>${state}</option>
                @endfor
            </select>

            <label>Use Scope</label>
            <select name="useScope">
                <option value="">Select scope</option>
                @for (var scope : useScopes)
                    <option value="${scope}" @if (scope.equalsIgnoreCase(request.getUseScope()))selected@end>${scope}</option>
                @endfor
            </select>

            <label>Existing Treatment</label>
            <select name="existingTreatment">
                @for (var treatment : existingTreatments)
                    <option value="${treatment}" @if (treatment.equalsIgnoreCase(request.getExistingTreatment()))selected@end>${treatment}</option>
                @endfor
            </select>

            <div class="panel" style="margin-top: 12px;">
                <p class="small muted">Vulnerable household flags</p>
                <label><input type="checkbox" name="infantPresent" value="true" @if (request.isInfantPresent())checked@end> infant present</label>
                <label><input type="checkbox" name="pregnancyPresent" value="true" @if (request.isPregnancyPresent())checked@end> pregnancy present</label>
                <label><input type="checkbox" name="immunocompromisedPresent" value="true" @if (request.isImmunocompromisedPresent())checked@end> immunocompromised present</label>
            </div>

            <button type="submit">Run Decision Engine</button>
        </form>
    </section>
`)
