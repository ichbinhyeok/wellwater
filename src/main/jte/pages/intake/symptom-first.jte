@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> symptoms

@template.layout.main(title = "Symptom-First Well Water Tool", robots = "index,follow", description = "Start from symptom and triage into a decision path.", content = @`
    <section class="panel">
        <h1>Symptom-First Decision Entry</h1>
        <p class="muted">Start with what you see or smell. The engine will suggest urgency, scope, and next action.</p>
    </section>

    <section class="panel">
        <form method="post" action="/tool/result">
            <input type="hidden" name="entryMode" value="symptom-first">
            <input type="hidden" name="slugHint" value="${request.getSlugHint()}">

            <label>Primary Symptom</label>
            <select name="symptomFlag">
                <option value="">Select symptom</option>
                @for (var symptom : symptoms)
                    <option value="${symptom}" @if (symptom.equalsIgnoreCase(request.getSymptomFlag()))selected@end>${symptom}</option>
                @endfor
            </select>

            <label>Sample Source</label>
            <select name="sampleSource">
                <option value="unknown">unknown</option>
                <option value="raw well">raw well</option>
                <option value="treated tap">treated tap</option>
            </select>

            <label>Lab Certified</label>
            <select name="labCertified">
                <option value="unknown">unknown</option>
                <option value="yes">yes</option>
                <option value="no">no</option>
            </select>

            <div class="panel" style="margin-top: 12px;">
                <p class="small muted">Vulnerable household flags</p>
                <label><input type="checkbox" name="infantPresent" value="true"> infant present</label>
                <label><input type="checkbox" name="pregnancyPresent" value="true"> pregnancy present</label>
                <label><input type="checkbox" name="immunocompromisedPresent" value="true"> immunocompromised present</label>
            </div>

            <button type="submit">Run Symptom Triage</button>
        </form>
    </section>
`)

