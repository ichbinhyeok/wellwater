@param com.example.wellwater.decision.model.DecisionResult result
@param String sessionId
@param java.util.List<com.example.wellwater.web.tool.ToolController.RenderableCta> ctaLinks

@template.layout.main(title = "Decision Result", robots = "noindex,nofollow", description = "Personalized well-water decision output.", content = @`
    <section class="panel">
        <p class="small muted">session: ${sessionId}</p>
        <h1>${result.primaryVerdictLabel()}</h1>
        <p>${result.primaryVerdictSentence()}</p>
        <p class="small">
            <span class="chip">${result.tier().label()}</span>
            <span class="chip">${result.confidence().label()}</span>
            <span class="chip">${result.branch().label()}</span>
            <span class="chip">${result.urgency().wireValue()}</span>
            <span class="chip">${result.scope().wireValue()}</span>
            <span class="chip">${result.problemType().wireValue()}</span>
            <span class="chip">${result.actionMode().wireValue()}</span>
        </p>
    </section>

    <section class="panel">
        <h2>Why This Verdict</h2>
        <ul>
            @for (var reason : result.keyReasons())
                <li>${reason}</li>
            @endfor
        </ul>
    </section>

    <section class="panel">
        <h2>Action Timeline</h2>
        <h3>Today</h3>
        <ul>
            @for (var item : result.todayActions())
                <li>${item}</li>
            @endfor
        </ul>
        <h3>This Week</h3>
        <ul>
            @for (var item : result.thisWeekActions())
                <li>${item}</li>
            @endfor
        </ul>
        <h3>Later</h3>
        <ul>
            @for (var item : result.laterActions())
                <li>${item}</li>
            @endfor
        </ul>
    </section>

    <section class="panel">
        <h2>Scenario Compare</h2>
        <div class="grid">
            @for (var scenario : result.scenarios())
                <article class="panel">
                    <h3>${scenario.scenarioTitle()}</h3>
                    <p class="small muted">${scenario.scenarioType()}</p>
                    <p>${scenario.fitReason()}</p>
                    <p class="small warn">Limitation: ${scenario.limitations()}</p>
                    <p class="small">scope: ${scenario.recommendedScope()}</p>
                    <p class="small">cost: ${scenario.estimatedCostBand()} / maintenance: ${scenario.estimatedMaintenanceBand()}</p>
                    <p class="small">next: ${scenario.nextAction()}</p>
                    <p class="small muted">claims:</p>
                    <ul>
                        @for (var claim : scenario.claimRequirements())
                            <li class="small">${claim}</li>
                        @endfor
                    </ul>
                </article>
            @endfor
        </div>
    </section>

    <section class="panel">
        <h2>Cost And Maintenance</h2>
        <p>${result.costNote()}</p>
    </section>

    <section class="panel">
        <h2>Sources And Assumptions</h2>
        <h3>Sources</h3>
        <ul>
            @for (var source : result.sourcesUsed())
                <li><a href="${source}">${source}</a></li>
            @endfor
        </ul>

        <h3>Assumptions</h3>
        <ul>
            @for (var item : result.assumptions())
                <li>${item}</li>
            @endfor
        </ul>

        <h3>Data Quality Notes</h3>
        <ul>
            @for (var item : result.dataQualityNotes())
                <li>${item}</li>
            @endfor
        </ul>

        <p class="small muted">${result.disclosureText()}</p>
    </section>

    <section class="panel">
        <h2>Next CTA</h2>
        <ul>
            @for (var cta : ctaLinks)
                <li><a href="${cta.outboundUrl()}">${cta.label()}</a> <span class="small muted">(${cta.type()})</span></li>
            @endfor
        </ul>
    </section>
`)

