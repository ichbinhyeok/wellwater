@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> symptoms
@param java.util.List<String> states
@param java.util.List<String> useScopes
@param java.util.List<String> existingTreatments

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
                <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getSampleSource()))selected@end>unknown</option>
                <option value="raw well" @if ("raw well".equalsIgnoreCase(request.getSampleSource()))selected@end>raw well</option>
                <option value="treated tap" @if ("treated tap".equalsIgnoreCase(request.getSampleSource()))selected@end>treated tap</option>
            </select>

            <label>Lab Certified</label>
            <select name="labCertified">
                <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getLabCertified()))selected@end>unknown</option>
                <option value="yes" @if ("yes".equalsIgnoreCase(request.getLabCertified()))selected@end>yes</option>
                <option value="no" @if ("no".equalsIgnoreCase(request.getLabCertified()))selected@end>no</option>
            </select>

            <label>Sample Date (optional)</label>
            <input type="date" name="sampleDate" value="${request.getSampleDate()}">

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

            <button type="submit">Run Symptom Triage</button>
        </form>
    </section>
`)
