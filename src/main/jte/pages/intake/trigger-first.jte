@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> triggers

@template.layout.main(title = "Trigger-First Well Water Tool", robots = "index,follow", description = "Start from event trigger and route to safe next actions.", content = @`
    <section class="panel">
        <h1>Trigger-First Decision Entry</h1>
        <p class="muted">Use this when flood, repair, wildfire, or sudden change happened.</p>
    </section>

    <section class="panel">
        <form method="post" action="/tool/result">
            <input type="hidden" name="entryMode" value="trigger-first">
            <input type="hidden" name="slugHint" value="${request.getSlugHint()}">

            <label>Primary Trigger</label>
            <select name="triggerFlag">
                <option value="">Select trigger</option>
                @for (var trigger : triggers)
                    <option value="${trigger}" @if (trigger.equalsIgnoreCase(request.getTriggerFlag()))selected@end>${trigger}</option>
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

            <button type="submit">Run Trigger Triage</button>
        </form>
    </section>
`)

