@param com.example.wellwater.decision.model.DecisionResult result
@param String sessionId
@param java.util.List<com.example.wellwater.web.result.RenderableCta> ctaLinks
@param String savedResultUrl
@param String shareUrl
@param String pdfUrl
@param String savedAtLabel
@param String expiresLabel
@param boolean sharedView
@param com.example.wellwater.lead.LeadCaptureContext leadContext
@param String leadStatus
@param String googleAnalyticsId

@template.layout.engine(
    title = result.browserTitle(),
    robots = "noindex,nofollow",
    description = "Personalized well-water decision output.",
    activeEntryMode = result.entryMode().wireValue(),
    googleAnalyticsId = googleAnalyticsId,
    leftPanel = @`
        <div class="hidden lg:flex flex-1 flex-col justify-center max-w-sm mx-auto w-full">
            <div class="mb-8">
                <span class="inline-block p-3 rounded-2xl bg-zinc-100 mb-4 ring-1 ring-zinc-200 shadow-sm">
                    <svg class="w-6 h-6 text-zinc-900" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                </span>
                <h2 class="text-3xl font-bold text-zinc-900 tracking-tight mb-3">Decision result</h2>
                <p class="text-zinc-500 leading-relaxed text-sm">${result.resultIntroSentence()}</p>
            </div>

            <div class="bg-zinc-50 rounded-2xl border border-zinc-200 p-5 mb-8">
                <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-4">Decision Summary</p>
                <div class="space-y-3">
                    <div class="flex justify-between items-center pb-3 border-b border-zinc-200/60">
                        <span class="text-sm font-medium text-zinc-500">Priority</span>
                        <span class="text-sm font-semibold text-zinc-900">${result.consumerPriorityLabel()}</span>
                    </div>
                    <div class="flex justify-between items-center pb-3 border-b border-zinc-200/60">
                        <span class="text-sm font-medium text-zinc-500">Support</span>
                        <span class="text-sm font-semibold text-zinc-900">${result.consumerSupportLabel()}</span>
                    </div>
                    <div class="flex justify-between items-center pb-3 border-b border-zinc-200/60">
                        <span class="text-sm font-medium text-zinc-500">Likely Scope</span>
                        <span class="text-sm font-semibold text-zinc-900">${result.consumerScopeLabel()}</span>
                    </div>
                    <div class="flex justify-between items-center">
                        <span class="text-sm font-medium text-zinc-500">Confidence</span>
                        <span class="text-sm font-semibold text-zinc-900">${result.confidence().label()}</span>
                    </div>
                </div>
            </div>

            <a href="/tool/${result.entryMode().wireValue()}" class="flex items-center justify-center w-full py-3.5 border border-zinc-300 rounded-xl text-sm font-bold text-zinc-900 hover:bg-zinc-50 hover:border-zinc-400 transition-colors shadow-sm">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path></svg>
                Enter new data
            </a>
        </div>

        <div class="lg:hidden w-full space-y-4">
            <div class="rounded-[26px] border border-zinc-200 bg-white/90 p-5 shadow-subtle">
                <div class="flex items-start justify-between gap-3 mb-4">
                    <div>
                        <span class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[11px] font-bold ${result.branch().name().equals("RED") ? "bg-danger/10 text-danger" : (result.branch().name().equals("GREEN") ? "bg-safe/10 text-safe" : "bg-warn/10 text-warn")} uppercase tracking-wide">
                            ${result.consumerSupportLabel()}
                        </span>
                        <h2 class="text-2xl font-bold text-zinc-900 tracking-tight mt-3">Result ready</h2>
                    </div>
                    <span class="inline-flex items-center rounded-full border border-zinc-200 bg-zinc-50 px-2.5 py-1 text-[11px] font-bold uppercase tracking-wide text-zinc-500">${result.confidence().label()}</span>
                </div>
                <p class="text-sm text-zinc-600 leading-relaxed">${result.resultIntroSentence()}</p>
                <div class="grid grid-cols-2 gap-3 mt-5">
                    <div class="rounded-2xl border border-zinc-200 bg-zinc-50 p-3">
                        <p class="text-[11px] font-bold uppercase tracking-widest text-zinc-400 mb-1">Priority</p>
                        <p class="text-sm font-semibold text-zinc-900">${result.consumerPriorityLabel()}</p>
                    </div>
                    <div class="rounded-2xl border border-zinc-200 bg-zinc-50 p-3">
                        <p class="text-[11px] font-bold uppercase tracking-widest text-zinc-400 mb-1">Support</p>
                        <p class="text-sm font-semibold text-zinc-900">${result.consumerSupportLabel()}</p>
                    </div>
                    <div class="rounded-2xl border border-zinc-200 bg-zinc-50 p-3">
                        <p class="text-[11px] font-bold uppercase tracking-widest text-zinc-400 mb-1">Scope</p>
                        <p class="text-sm font-semibold text-zinc-900">${result.consumerScopeLabel()}</p>
                    </div>
                    <div class="rounded-2xl border border-zinc-200 bg-zinc-50 p-3">
                        <p class="text-[11px] font-bold uppercase tracking-widest text-zinc-400 mb-1">Topic</p>
                        <p class="text-sm font-semibold text-zinc-900">${result.primaryTopicLabel()}</p>
                    </div>
                </div>
            </div>

            <a href="/tool/${result.entryMode().wireValue()}" class="flex items-center justify-center w-full py-3.5 border border-zinc-300 rounded-xl text-sm font-bold text-zinc-900 hover:bg-zinc-50 hover:border-zinc-400 transition-colors shadow-sm bg-white/90">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path></svg>
                Enter new data
            </a>
        </div>
    `,
    rightPanel = @`
        <div class="px-4 sm:px-6 lg:px-0 pb-10 lg:pb-12 h-auto overflow-visible lg:overflow-y-auto no-scrollbar bg-zinc-50 lg:bg-transparent"
             data-analytics-result-root="true"
             data-entry-mode="${result.entryMode().wireValue()}"
             data-branch="${result.branch().label()}"
             data-tier="${result.tier().label()}"
             data-problem-type="${result.problemType().wireValue()}"
             data-shared-view="${sharedView}">

            <div class="lg:hidden pt-4 pb-5">
                <div class="rounded-[24px] border border-zinc-200 bg-white/88 p-4 shadow-sm">
                    <div class="flex items-center justify-between gap-3 mb-3">
                        <div>
                            <p class="text-[11px] font-bold uppercase tracking-[0.18em] text-zinc-400 mb-1">Analysis report</p>
                            <p class="text-base font-semibold tracking-tight text-zinc-900">${result.primaryTopicLabel()}</p>
                        </div>
                        <span class="inline-flex items-center gap-2 rounded-full border border-zinc-200 bg-zinc-50 px-2.5 py-1 text-[11px] font-semibold text-zinc-600">
                            <span class="w-2 h-2 rounded-full bg-emerald-500"></span>
                            Ready
                        </span>
                    </div>
                    <p class="text-sm text-zinc-600 leading-relaxed">${result.mobileAnalysisSummary()}</p>
                    <div class="flex flex-wrap gap-2 mt-4">
                        @if(result.hasRecommendedTestCards() || !result.recommendedTests().isEmpty())
                            <a href="#recommended-tests" class="inline-flex items-center rounded-full border border-zinc-300 bg-white px-3 py-2 text-xs font-semibold text-zinc-900">Recommended tests</a>
                        @endif
                        @if(!result.compareReadinessChecks().isEmpty())
                            <a href="#compare-readiness" class="inline-flex items-center rounded-full border border-zinc-300 bg-white px-3 py-2 text-xs font-semibold text-zinc-900">Compare gates</a>
                        @endif
                        <a href="#method-and-trust" class="inline-flex items-center rounded-full border border-zinc-300 bg-white px-3 py-2 text-xs font-semibold text-zinc-900">Trust surface</a>
                    </div>
                </div>
            </div>

            <div class="pt-6 lg:pt-0 pb-6 flex items-end justify-between border-b border-zinc-200/60 mb-8 hidden lg:flex">
                <div>
                    <h2 class="text-xs font-bold text-zinc-400 uppercase tracking-[0.15em] mb-1">Analysis Report</h2>
                    <p class="text-xl font-bold text-zinc-900 tracking-tight">${result.primaryTopicLabel()}</p>
                </div>
                <div class="text-right">
                    <div class="flex items-center gap-2 text-sm text-zinc-500 font-medium">
                        <span class="relative flex h-2 w-2">
                          <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
                          <span class="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
                        </span>
                        Analysis completed
                    </div>
                </div>
            </div>

            <div class="space-y-6 pt-4 lg:pt-0">

                <!-- Block 1: Primary Verdict -->
                <div id="analysis-verdict" class="bg-white rounded-2xl p-6 lg:p-8 border ${result.branch().name().equals("RED") ? "border-danger/30 ring-danger/10 ring-1" : "border-zinc-200"} shadow-subtle relative overflow-hidden">
                    @if(result.branch().name().equals("RED"))
                        <div class="absolute top-0 left-0 w-1.5 h-full bg-danger"></div>
                    @elseif(result.branch().name().equals("AMBER"))
                        <div class="absolute top-0 left-0 w-1.5 h-full bg-warn"></div>
                    @else
                        <div class="absolute top-0 left-0 w-1.5 h-full bg-safe"></div>
                    @endif

                    <div class="flex items-center gap-3 mb-5">
                        <span class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold ${result.branch().name().equals("RED") ? "bg-danger/10 text-danger" : (result.branch().name().equals("GREEN") ? "bg-safe/10 text-safe" : "bg-warn/10 text-warn")} uppercase tracking-wider">
                            @if(result.branch().name().equals("RED"))
                                <span class="w-2 h-2 rounded-full bg-danger animate-pulse"></span>
                            @else
                                <span class="w-1.5 h-1.5 rounded-full bg-current"></span>
                            @endif
                            ${result.consumerSupportLabel()}
                        </span>
                        <span class="text-xs font-bold text-zinc-400 uppercase tracking-widest">${result.confidence().label()}</span>
                    </div>

                    <h3 class="text-2xl lg:text-3xl font-bold tracking-tight text-zinc-900 mb-4">${result.primaryVerdictLabel()}</h3>
                    <p class="text-zinc-600 leading-relaxed text-sm lg:text-base">${result.primaryVerdictSentence()}</p>

                    @if(!result.keyReasons().isEmpty())
                        <div class="mt-5 pt-5 border-t border-zinc-100">
                            <ul class="space-y-2">
                                @for (var reason : result.keyReasons())
                                    <li class="flex items-start gap-2 text-sm text-zinc-600">
                                        <svg class="w-4 h-4 text-zinc-400 mt-0.5 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                                        ${reason}
                                    </li>
                                @endfor
                            </ul>
                        </div>
                    @endif
                </div>

                <!-- Block 2: Action Timeline -->
                <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm">
                    <h4 id="action-timeline" class="text-sm font-bold text-zinc-900 mb-6 flex items-center uppercase tracking-wider">
                        <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                        Action Timeline
                    </h4>

                    <div class="relative border-l-2 border-zinc-100 ml-3 space-y-7">
                        @if(!result.todayActions().isEmpty())
                            <div class="relative pl-6">
                                <div class="absolute w-3 h-3 bg-white border-[3px] border-danger rounded-full -left-[7.5px] top-1"></div>
                                <div class="text-[10px] font-bold text-danger uppercase tracking-widest mb-1.5">Today</div>
                                <ul class="space-y-2">
                                    @for(var item : result.todayActions())
                                        <li class="text-sm text-zinc-700 leading-relaxed font-medium">${item}</li>
                                    @endfor
                                </ul>
                            </div>
                        @endif

                        @if(!result.thisWeekActions().isEmpty())
                            <div class="relative pl-6">
                                <div class="absolute w-3 h-3 bg-white border-[3px] border-warn rounded-full -left-[7.5px] top-1"></div>
                                <div class="text-[10px] font-bold text-warn uppercase tracking-widest mb-1.5">This Week</div>
                                <ul class="space-y-2">
                                    @for(var item : result.thisWeekActions())
                                        <li class="text-sm text-zinc-700 leading-relaxed">${item}</li>
                                    @endfor
                                </ul>
                            </div>
                        @endif

                        @if(!result.laterActions().isEmpty())
                            <div class="relative pl-6">
                                <div class="absolute w-3 h-3 bg-white border-[3px] border-zinc-300 rounded-full -left-[7.5px] top-1"></div>
                                <div class="text-[10px] font-bold text-zinc-500 uppercase tracking-widest mb-1.5">Next 30 Days</div>
                                <ul class="space-y-2">
                                    @for(var item : result.laterActions())
                                        <li class="text-sm text-zinc-600 leading-relaxed">${item}</li>
                                    @endfor
                                </ul>
                            </div>
                        @endif
                    </div>
                </div>

                @if(result.hasRecommendedTestCards() || !result.recommendedTests().isEmpty())
                    <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm">
                        <h4 id="recommended-tests" class="text-sm font-bold text-zinc-900 mb-5 flex items-center uppercase tracking-wider">
                            <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a4 4 0 00-5.656 0L12 17.2l-1.772-1.772a4 4 0 00-5.656 5.656L12 28.312l7.428-7.228a4 4 0 000-5.656z" transform="translate(0 -6) scale(.8)"></path></svg>
                            Recommended Tests
                        </h4>
                        @if(result.hasRecommendedTestOrderNote())
                            <p class="text-sm text-zinc-600 leading-relaxed mb-4">${result.recommendedTestOrderNote()}</p>
                        @endif
                        <div class="space-y-3">
                            @if(result.hasRecommendedTestCards())
                                @for(int i = 0; i < result.recommendedTestCards().size(); i++)
                                    !{var test = result.recommendedTestCards().get(i);}
                                    <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                        <div class="flex items-center justify-between gap-3 mb-2">
                                            <p class="text-sm font-semibold text-zinc-900">${test.testName()}</p>
                                            <span class="inline-flex items-center rounded-full border border-zinc-200 bg-white px-2.5 py-1 text-[11px] font-bold uppercase tracking-wider text-zinc-500">Step ${i + 1}</span>
                                        </div>
                                        <p class="text-sm text-zinc-700 leading-relaxed mt-2">${test.whyNow()}</p>
                                        <p class="text-xs text-zinc-500 mt-3"><span class="font-semibold uppercase tracking-wide text-zinc-400">Sample plan</span> ${test.samplePlan()}</p>
                                        @if(test.hasResourceLink())
                                            <a href="${test.resourceUrl()}" class="mt-3 inline-flex items-center justify-center rounded-lg border border-zinc-300 bg-white px-3 py-2 text-xs font-semibold text-zinc-900 hover:border-zinc-400 hover:bg-zinc-100 transition-colors">
                                                ${test.resourceLabel()}
                                            </a>
                                        @endif
                                    </div>
                                @endfor
                            @else
                                @for(var test : result.recommendedTests())
                                    <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                        <p class="text-sm text-zinc-700 leading-relaxed">${test}</p>
                                    </div>
                                @endfor
                            @endif
                        </div>
                    </div>
                @endif

                @if(!result.compareReadinessChecks().isEmpty())
                    <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm">
                        <h4 id="compare-readiness" class="text-sm font-bold text-zinc-900 mb-5 flex items-center uppercase tracking-wider">
                            <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path></svg>
                            Before You Compare Or Buy
                        </h4>
                        <div class="space-y-3">
                            @for(var check : result.compareReadinessChecks())
                                <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                    <p class="text-sm text-zinc-700 leading-relaxed">${check}</p>
                                </div>
                            @endfor
                        </div>
                    </div>
                @endif

                @if(result.hasReportLinesReviewed())
                    <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm">
                        <h4 id="report-lines-reviewed" class="text-sm font-bold text-zinc-900 mb-5 flex items-center uppercase tracking-wider">
                            <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-7 4h8m-9-8h10"></path></svg>
                            Report Lines Reviewed
                        </h4>
                        <div class="space-y-3">
                            @for(var line : result.reportLinesReviewed())
                                <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                    <div class="flex items-center justify-between gap-3 mb-2">
                                        <p class="text-sm font-semibold text-zinc-900">${line.analyteLabel()}</p>
                                        <span class="inline-flex items-center rounded-full border border-zinc-200 bg-white px-2.5 py-1 text-[11px] font-bold uppercase tracking-wider text-zinc-500">
                                            ${line.primary() ? "Primary line" : "Supporting line"}
                                        </span>
                                    </div>
                                    <p class="text-sm text-zinc-700">${line.observedValueLabel()}</p>
                                    <p class="text-xs text-zinc-500 mt-2">${line.interpretationNote()}</p>
                                </div>
                            @endfor
                        </div>
                    </div>
                @endif

                @if(!result.consumerSupportingSignals().isEmpty())
                    <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm">
                        <h4 id="report-context-used" class="text-sm font-bold text-zinc-900 mb-5 flex items-center uppercase tracking-wider">
                            <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h10m-7 4h7"></path></svg>
                            Report Context Used
                        </h4>
                        <p class="text-sm text-zinc-600 leading-relaxed mb-4">These extra report lines or event clues changed the interpretation so the engine would not overfit a single result.</p>
                        <div class="flex flex-wrap gap-2">
                            @for(var signalLabel : result.consumerSupportingSignals())
                                <span class="inline-flex items-center rounded-full border border-zinc-200 bg-zinc-50 px-3 py-1 text-xs font-semibold text-zinc-700">${signalLabel}</span>
                            @endfor
                        </div>
                    </div>
                @endif

                @if(result.hasSoftenerSizingPreview())
                    <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm">
                        <h4 id="softener-sizing-preview" class="text-sm font-bold text-zinc-900 mb-5 flex items-center uppercase tracking-wider">
                            <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v18m-7-7h14"></path></svg>
                            Softener Sizing Preview
                        </h4>
                        <p class="text-sm text-zinc-600 leading-relaxed mb-4">${result.softenerSizingPreview().summary()}</p>
                        @if(result.softenerSizingPreview().eligible())
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-3 mb-4">
                                <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                    <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Recommended class</p>
                                    <p class="text-sm font-semibold text-zinc-900">${result.softenerSizingPreview().recommendedClass()}</p>
                                </div>
                                <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                    <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Sizing verdict</p>
                                    <p class="text-sm font-semibold text-zinc-900">${result.softenerSizingPreview().sizingVerdict()}</p>
                                </div>
                                <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                    <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Hardness basis</p>
                                    <p class="text-sm font-semibold text-zinc-900">${result.softenerSizingPreview().hardnessLabel()}</p>
                                </div>
                                <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                    <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Daily grain load</p>
                                    <p class="text-sm font-semibold text-zinc-900">${result.softenerSizingPreview().dailyGrainLoadLabel()}</p>
                                </div>
                            </div>
                            <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4 mb-4">
                                <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Regeneration pace</p>
                                <p class="text-sm font-semibold text-zinc-900">${result.softenerSizingPreview().regenerationLabel()}</p>
                            </div>
                        @else
                            <div class="rounded-xl border border-warn/20 bg-warn/5 p-4 mb-4">
                                <p class="text-sm font-semibold text-zinc-900">${result.softenerSizingPreview().headline()}</p>
                                <p class="text-sm text-zinc-600 mt-2">${result.softenerSizingPreview().sizingVerdict()}</p>
                            </div>
                        @endif
                        @if(!result.softenerSizingPreview().notes().isEmpty())
                            <div class="space-y-3 mb-4">
                                @for(var note : result.softenerSizingPreview().notes())
                                    <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                                        <p class="text-sm text-zinc-700 leading-relaxed">${note}</p>
                                    </div>
                                @endfor
                            </div>
                        @endif
                        <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                            <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Next action</p>
                            <p class="text-sm font-semibold text-zinc-900">${result.softenerSizingPreview().nextAction()}</p>
                        </div>
                    </div>
                @endif

                <!-- Block 3: Scenario Compare -->
                @if(!result.scenarios().isEmpty())
                    <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm overflow-hidden">
                    <h4 id="scenario-compare" class="text-sm font-bold text-zinc-900 mb-5 flex items-center uppercase tracking-wider">
                            <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path></svg>
                            Scenario compare
                        </h4>

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            @for(int i = 0; i < result.scenarios().size(); i++)
                                !{var scenario = result.scenarios().get(i);}
                                <div class="border ${result.isRecommendedScenario(scenario) ? "border-[1.5px] border-zinc-900 bg-zinc-50 shadow-sm relative" : "border-zinc-200 hover:border-zinc-300"} rounded-xl p-5 flex flex-col transition-all">
                                    @if(result.isRecommendedScenario(scenario))
                                        <div class="absolute -top-2.5 right-4 bg-zinc-900 text-white text-[10px] font-bold px-2.5 py-0.5 rounded-full tracking-wider">BEST FIT</div>
                                    @endif

                                    <div class="text-[10px] font-bold text-zinc-500 uppercase tracking-widest mb-1.5">${scenario.scenarioType()}</div>
                                    <div class="font-bold text-zinc-900 mb-2">${scenario.scenarioTitle()}</div>
                                    <div class="text-sm text-zinc-600 mb-4 flex-1 leading-relaxed">${scenario.fitReason()}</div>

                                    <div class="pt-4 border-t ${result.isRecommendedScenario(scenario) ? "border-zinc-200" : "border-zinc-100"} flex flex-col gap-2 mt-auto">
                                        <div class="flex items-center justify-between">
                                            <span class="text-xs font-semibold text-zinc-500 w-16">Scope</span>
                                            <span class="text-xs font-medium text-zinc-900 text-right">${scenario.recommendedScope()}</span>
                                        </div>
                                        <div class="flex items-center justify-between">
                                            <span class="text-xs font-semibold text-zinc-500 w-16">Upfront cost</span>
                                            <span class="text-xs font-bold text-zinc-900 text-right">${scenario.estimatedCostBand()}</span>
                                        </div>
                                        <div class="flex items-center justify-between">
                                            <span class="text-xs font-semibold text-zinc-500 w-16">Maintenance</span>
                                            <span class="text-xs font-bold text-zinc-900 text-right">${scenario.estimatedMaintenanceBand()}</span>
                                        </div>
                                        <div class="mt-2 text-xs text-warn font-semibold bg-warn/5 p-2 rounded-lg border border-warn/10">Limitation: ${scenario.limitations()}</div>
                                    </div>
                                </div>
                            @endfor
                        </div>
                    </div>
                @endif

                <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm">
                    <h4 id="save-and-share" class="text-sm font-bold text-zinc-900 mb-3 flex items-center uppercase tracking-wider">
                        <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-3-3v6m8 2a2 2 0 01-2 2H6a2 2 0 01-2-2V7a2 2 0 012-2h3.586a1 1 0 01.707.293l1.414 1.414A1 1 0 0012.414 7H18a2 2 0 012 2v8z"></path></svg>
                        Save & Share Surface
                    </h4>
                    <p class="text-sm text-zinc-600 mb-4">This result is stored as a private noindex snapshot so you can reopen it, download it, or share it without turning it into a public landing page.</p>
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                        <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                            <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-2">Save Result</p>
                            <p class="text-sm text-zinc-700">Saved ${savedAtLabel}.</p>
                            @if(expiresLabel != null && !expiresLabel.isBlank())
                                <p class="text-xs text-zinc-500 mt-1">Available through ${expiresLabel}.</p>
                            @endif
                            <a href="${savedResultUrl}" class="mt-3 inline-flex items-center justify-center rounded-lg border border-zinc-300 bg-white px-3 py-2 text-sm font-semibold text-zinc-900 hover:border-zinc-400 hover:bg-zinc-100 transition-colors">
                                ${sharedView ? "Open saved view" : "Review saved view"}
                            </a>
                        </div>
                        <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                            <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-2">Download PDF</p>
                            <p class="text-sm text-zinc-700">Export the verdict, timeline, links, and disclosure block for offline review.</p>
                            <a href="${pdfUrl}" class="mt-3 inline-flex items-center justify-center rounded-lg border border-zinc-300 bg-white px-3 py-2 text-sm font-semibold text-zinc-900 hover:border-zinc-400 hover:bg-zinc-100 transition-colors" target="_blank" rel="nofollow">
                                Download PDF
                            </a>
                        </div>
                        <div class="rounded-xl border border-zinc-200 bg-zinc-50 p-4">
                            <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-2">Share Link</p>
                            <p class="text-sm text-zinc-700 mb-3">Copy a private link for a family member, installer, or follow-up reviewer.</p>
                            <label class="sr-only" for="share-link-input">Share link</label>
                            <input id="share-link-input" type="text" readonly value="${shareUrl}" class="w-full rounded-lg border border-zinc-200 bg-white px-3 py-2 text-xs text-zinc-600 mb-3">
                            <button id="share-link-button" type="button" onclick="copyShareLink()" class="inline-flex items-center justify-center rounded-lg border border-zinc-300 bg-white px-3 py-2 text-sm font-semibold text-zinc-900 hover:border-zinc-400 hover:bg-zinc-100 transition-colors">
                                Copy link
                            </button>
                        </div>
                    </div>
                </div>

                @if(leadContext != null)
                    <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm" id="lead-capture">
                        <h4 id="follow-up" class="text-sm font-bold text-zinc-900 mb-3 flex items-center uppercase tracking-wider">
                            <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 1118 0z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10a3 3 0 100 6 3 3 0 000-6z"></path></svg>
                            Follow-Up
                        </h4>
                        <p class="text-sm text-zinc-600 leading-relaxed">${leadContext.summary()}</p>
                        @if("success".equals(leadStatus))
                            <p class="text-sm font-semibold text-safe mt-4">Follow-up request captured. The saved result link kept the diagnostic context attached.</p>
                        @elseif("invalid".equals(leadStatus))
                            <p class="text-sm font-semibold text-warn mt-4">Enter a valid email address to submit this follow-up request.</p>
                        @endif
                        <form method="post" action="/lead/submit" class="space-y-4 mt-5">
                            <input type="hidden" name="redirectPath" value="${leadContext.redirectPath()}">
                            <input type="hidden" name="sourceType" value="${leadContext.sourceType()}">
                            <input type="hidden" name="sourceFamily" value="${leadContext.sourceFamily()}">
                            <input type="hidden" name="sourceSlug" value="${leadContext.sourceSlug()}">
                            <input type="hidden" name="sourceLabel" value="${leadContext.sourceLabel()}">
                            <input type="text" name="website" tabindex="-1" autocomplete="off" style="position:absolute;left:-9999px;width:1px;height:1px;opacity:0;" aria-hidden="true">
                            <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                                <input type="email" name="email" placeholder="you@example.com" required class="w-full rounded-xl border border-zinc-200 bg-zinc-50 px-4 py-3 text-sm text-zinc-900">
                                <input type="text" name="state" placeholder="State (optional)" maxlength="12" class="w-full rounded-xl border border-zinc-200 bg-zinc-50 px-4 py-3 text-sm text-zinc-900">
                            </div>
                            <input type="text" name="name" placeholder="Name (optional)" class="w-full rounded-xl border border-zinc-200 bg-zinc-50 px-4 py-3 text-sm text-zinc-900">
                            <textarea name="note" rows="4" placeholder="What follow-up do you want on this result?" class="w-full rounded-xl border border-zinc-200 bg-zinc-50 px-4 py-3 text-sm text-zinc-900"></textarea>
                            <button type="submit" class="inline-flex items-center justify-center rounded-xl bg-zinc-900 px-5 py-3 text-sm font-semibold text-white hover:bg-zinc-800 transition-colors">
                                ${leadContext.submitLabel()}
                            </button>
                        </form>
                    </div>
                @endif

                <!-- Additional Context -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div class="bg-white rounded-2xl p-5 border border-zinc-200 shadow-sm">
                        <h4 id="data-quality" class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-3">Data Quality</h4>
                        <div class="space-y-2 text-sm text-zinc-600">
                            <div class="flex justify-between"><span class="text-zinc-500">Sample freshness</span> <span class="font-medium text-zinc-900">${result.sampleFreshness()}</span></div>
                            <div class="flex justify-between"><span class="text-zinc-500">Completeness</span> <span class="font-medium text-zinc-900">${result.completenessScore()}/100</span></div>
                            @if(result.hasThresholdSummary())
                                <div class="flex justify-between gap-3"><span class="text-zinc-500">Threshold Check</span> <span class="font-medium text-zinc-900 text-right">${result.thresholdSummary()}</span></div>
                            @endif
                            <div class="flex justify-between gap-3"><span class="text-zinc-500">Rule Version</span> <span class="font-medium text-zinc-900 text-right">${result.decisionVersion()}</span></div>
                            @for(var note : result.dataQualityNotes())
                                <p class="text-xs italic text-zinc-500 mt-2">${note}</p>
                            @endfor
                        </div>
                    </div>

                    <div class="bg-white rounded-2xl p-5 border border-zinc-200 shadow-sm">
                        <h4 id="links-and-sources" class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-3">Links & Sources</h4>
                        <div class="space-y-3 text-sm">
                            <a href="${result.localGuidanceUrl()}" class="flex items-center text-zinc-600 hover:text-zinc-900 transition-colors">
                                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"></path></svg>
                                State guidance
                            </a>
                            <a href="${result.certifiedLabUrl()}" class="flex items-center text-zinc-600 hover:text-zinc-900 transition-colors">
                                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"></path></svg>
                                Certified lab finder
                            </a>
                        </div>
                    </div>
                </div>

                <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm">
                    <h4 id="method-and-trust" class="text-sm font-bold text-zinc-900 mb-3 flex items-center uppercase tracking-wider">
                        <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 11c0 1.657-1.79 3-4 3s-4-1.343-4-3 1.79-3 4-3 4 1.343 4 3zm8 0c0 1.657-1.79 3-4 3s-4-1.343-4-3 1.79-3 4-3 4 1.343 4 3zm-8 7c0 1.657-1.79 3-4 3s-4-1.343-4-3 1.79-3 4-3 4 1.343 4 3zm8 0c0 1.657-1.79 3-4 3s-4-1.343-4-3 1.79-3 4-3 4 1.343 4 3z"></path></svg>
                        Method And Trust
                    </h4>
                    <p class="text-sm text-zinc-600 mb-4">Use the public trust surface when you want to check how evidence, review, storage, and commercial routing are handled on this result path.</p>
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                        <a href="/trust/methodology" class="rounded-xl border border-zinc-200 bg-zinc-50 p-4 hover:border-zinc-300 transition-colors">
                            <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Methodology</p>
                            <p class="text-sm font-semibold text-zinc-900">See how the decision engine handles evidence and buying gates.</p>
                        </a>
                        <a href="/trust/reviewers-and-expertise" class="rounded-xl border border-zinc-200 bg-zinc-50 p-4 hover:border-zinc-300 transition-colors">
                            <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Reviewers</p>
                            <p class="text-sm font-semibold text-zinc-900">Check who reviews health, nuisance, and claim-check logic.</p>
                        </a>
                        <a href="/trust/privacy-and-data-handling" class="rounded-xl border border-zinc-200 bg-zinc-50 p-4 hover:border-zinc-300 transition-colors">
                            <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Privacy</p>
                            <p class="text-sm font-semibold text-zinc-900">Review how saved results, follow-up requests, and analytics are stored.</p>
                        </a>
                        <a href="/trust/commercial-disclosure" class="rounded-xl border border-zinc-200 bg-zinc-50 p-4 hover:border-zinc-300 transition-colors">
                            <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-1">Commercial disclosure</p>
                            <p class="text-sm font-semibold text-zinc-900">See why compare and follow-up routes stay behind testing and scope control.</p>
                        </a>
                    </div>
                </div>

                <!-- Block 4: CTA -->
                @if(ctaLinks != null && !ctaLinks.isEmpty())
                    <div class="bg-zinc-900 rounded-2xl p-6 lg:p-8 text-center lg:text-left flex flex-col lg:flex-row items-center justify-between shadow-float mt-4">
                        <div class="mb-5 lg:mb-0 max-w-sm">
                            <h4 class="text-lg font-bold text-white mb-2 tracking-tight">Need more help?</h4>
                            <p class="text-zinc-400 text-sm leading-relaxed">Review certified guidance and trusted local resources before making any purchase decision.</p>
                        </div>
                        <div class="flex items-stretch sm:items-center gap-3 w-full lg:w-auto flex-col sm:flex-row sm:flex-wrap">
                            @for(var cta : ctaLinks)
                                @if(cta.type().contains("affiliate") || cta.type().contains("partner"))
                                    <a href="${cta.outboundUrl()}" class="w-full sm:w-auto max-w-full px-5 py-3 rounded-xl font-semibold text-sm bg-white text-zinc-900 hover:bg-zinc-100 transition-all shadow-[0_0_20px_rgba(255,255,255,0.15)] ring-1 ring-white/50 inline-block text-center">${cta.label()}</a>
                                @else
                                    <a href="${cta.outboundUrl()}" class="w-full sm:w-auto max-w-full px-5 py-3 rounded-xl font-semibold text-sm bg-zinc-800 text-white hover:bg-zinc-700 transition-colors border border-zinc-700 shadow-sm inline-block text-center">${cta.label()}</a>
                                @endif
                            @endfor
                        </div>
                    </div>
                @endif

                <p class="text-xs text-zinc-400 text-center mt-6 italic pb-8">
                    Disclosure: ${result.disclosureText()}
                </p>

            </div>
        </div>
        <script>
            async function copyShareLink() {
                const input = document.getElementById('share-link-input');
                const button = document.getElementById('share-link-button');
                if (!input || !button) {
                    return;
                }
                const raw = input.value || '';
                const absolute = raw.startsWith('http://') || raw.startsWith('https://') ? raw : window.location.origin + raw;
                try {
                    await navigator.clipboard.writeText(absolute);
                    input.value = absolute;
                    if (window.wvAnalytics && typeof window.wvAnalytics.track === 'function') {
                        const resultContext = window.wvAnalytics.resultContext ? window.wvAnalytics.resultContext() : {};
                        window.wvAnalytics.track('share', {
                            entry_mode: resultContext && resultContext.entry_mode,
                            result_tier: resultContext && resultContext.result_tier,
                            result_branch: resultContext && resultContext.result_branch,
                            problem_type: resultContext && resultContext.problem_type,
                            shared_view: resultContext && resultContext.shared_view,
                            method: 'copy_link',
                            content_type: 'saved_result'
                        });
                    }
                    button.textContent = 'Copied';
                    setTimeout(() => {
                        button.textContent = 'Copy link';
                    }, 1800);
                } catch (error) {
                    input.focus();
                    input.select();
                }
            }
        </script>
    `
)
