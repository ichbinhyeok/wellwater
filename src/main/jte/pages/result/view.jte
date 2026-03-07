@param com.example.wellwater.decision.model.DecisionResult result
@param String sessionId
@param java.util.List<com.example.wellwater.web.tool.ToolController.RenderableCta> ctaLinks

@template.layout.engine(
    title = "Decision Result",
    robots = "noindex,nofollow",
    description = "Personalized well-water decision output.",
    activeEntryMode = result.entryMode().wireValue(),
    leftPanel = @`
        <div class="flex-1 flex flex-col justify-center max-w-sm mx-auto w-full">
            <div class="mb-8">
                <span class="inline-block p-3 rounded-2xl bg-zinc-100 mb-4 ring-1 ring-zinc-200 shadow-sm">
                    <svg class="w-6 h-6 text-zinc-900" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                </span>
                <h2 class="text-3xl font-bold text-zinc-900 tracking-tight mb-3">Analysis Complete</h2>
                <p class="text-zinc-500 leading-relaxed text-sm">Your water quality data has been processed by the decision engine. Please review your personalized verdict and actions on the right.</p>
            </div>

            <div class="bg-zinc-50 rounded-2xl border border-zinc-200 p-5 mb-8">
                <p class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-4">Input Context</p>
                <div class="space-y-3">
                    <div class="flex justify-between items-center pb-3 border-b border-zinc-200/60">
                        <span class="text-sm font-medium text-zinc-500">Tier</span>
                        <span class="text-sm font-semibold text-zinc-900">${result.tier().label()}</span>
                    </div>
                    <div class="flex justify-between items-center pb-3 border-b border-zinc-200/60">
                        <span class="text-sm font-medium text-zinc-500">Branch</span>
                        <span class="text-sm font-semibold text-zinc-900">${result.branch().label()}</span>
                    </div>
                    <div class="flex justify-between items-center pb-3 border-b border-zinc-200/60">
                        <span class="text-sm font-medium text-zinc-500">Urgency</span>
                        <span class="text-sm font-semibold text-zinc-900">${result.urgency().wireValue()}</span>
                    </div>
                    <div class="flex justify-between items-center">
                        <span class="text-sm font-medium text-zinc-500">Session ID</span>
                        <span class="text-[10px] font-mono text-zinc-400 truncate w-32 text-right">${sessionId}</span>
                    </div>
                </div>
            </div>

            <a href="/tool/${result.entryMode().wireValue()}" class="flex items-center justify-center w-full py-3.5 border border-zinc-300 rounded-xl text-sm font-bold text-zinc-900 hover:bg-zinc-50 hover:border-zinc-400 transition-colors shadow-sm">
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path></svg>
                새로운 데이터 입력하기
            </a>
        </div>
    `,
    rightPanel = @`
        <!-- Mobile Bottom Sheet Drag Handle -->
        <div class="lg:hidden w-full flex justify-center pt-3 pb-4 bg-white/95 backdrop-blur-md rounded-t-2xl border-b border-zinc-100 cursor-pointer shadow-sm relative z-20" onclick="toggleBottomSheet()">
            <div class="w-12 h-1.5 bg-zinc-300 rounded-full"></div>
        </div>
        
        <!-- Mobile Sticky Header -->
        <div id="mobile-summary" class="lg:hidden px-6 pb-4 bg-white/95 backdrop-blur-md flex items-center justify-between border-b border-zinc-100 transition-opacity duration-300 relative z-20 cursor-pointer" onclick="toggleBottomSheet()">
            <div>
                <span class="inline-flex items-center gap-1.5 px-2 py-0.5 rounded-full text-[11px] font-bold ${result.branch().name().equals("RED") ? "bg-danger/10 text-danger" : (result.branch().name().equals("GREEN") ? "bg-safe/10 text-safe" : "bg-warn/10 text-warn")} mb-1 tracking-wide uppercase">
                    ${result.tier().label()}
                </span>
                <p class="text-sm font-semibold text-zinc-900 truncate w-48">${result.primaryVerdictLabel()}</p>
            </div>
            <button class="w-8 h-8 rounded-full bg-zinc-100 flex items-center justify-center text-zinc-900 shadow-sm border border-zinc-200">
                <svg class="w-4 h-4 transform transition-transform duration-300 rotate-180" id="chevron-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7"></path></svg>
            </button>
        </div>

        <div class="px-6 lg:px-0 pb-20 lg:pb-12 h-[calc(100vh-80px)] lg:h-auto overflow-y-auto no-scrollbar bg-zinc-50 lg:bg-transparent">
            
            <div class="pt-6 lg:pt-0 pb-6 flex items-end justify-between border-b border-zinc-200/60 mb-8 hidden lg:flex">
                <div>
                    <h2 class="text-xs font-bold text-zinc-400 uppercase tracking-[0.15em] mb-1">Analysis Report</h2>
                    <p class="text-xl font-bold text-zinc-900 tracking-tight">판정 결과</p>
                </div>
                <div class="text-right">
                    <div class="flex items-center gap-2 text-sm text-zinc-500 font-medium">
                        <span class="relative flex h-2 w-2">
                          <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
                          <span class="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
                        </span>
                        실시간 분석 완료
                    </div>
                </div>
            </div>

            <div class="space-y-6 pt-4 lg:pt-0">
                
                <!-- Block 1: Primary Verdict -->
                <div class="bg-white rounded-2xl p-6 lg:p-8 border ${result.branch().name().equals("RED") ? "border-danger/30 ring-danger/10 ring-1" : "border-zinc-200"} shadow-subtle relative overflow-hidden">
                    @if(result.branch().name().equals("RED"))
                        <div class="absolute top-0 left-0 w-1.5 h-full bg-danger"></div>
                    @else if(result.branch().name().equals("AMBER"))
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
                            ${result.tier().label()}
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
                    <h4 class="text-sm font-bold text-zinc-900 mb-6 flex items-center uppercase tracking-wider">
                        <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                        Action Timeline
                    </h4>
                    
                    <div class="relative border-l-2 border-zinc-100 ml-3 space-y-7">
                        @if(!result.todayActions().isEmpty())
                            <div class="relative pl-6">
                                <div class="absolute w-3 h-3 bg-white border-[3px] border-danger rounded-full -left-[7.5px] top-1"></div>
                                <div class="text-[10px] font-bold text-danger uppercase tracking-widest mb-1.5">Today (즉시)</div>
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

                <!-- Block 3: Scenario Compare -->
                @if(!result.scenarios().isEmpty())
                    <div class="bg-white rounded-2xl p-6 border border-zinc-200 shadow-sm overflow-hidden">
                        <h4 class="text-sm font-bold text-zinc-900 mb-5 flex items-center uppercase tracking-wider">
                            <svg class="w-4 h-4 mr-2 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path></svg>
                            해결 시나리오 비교
                        </h4>
                        
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            @for(int i = 0; i < result.scenarios().size(); i++)
                                !{var scenario = result.scenarios().get(i);}
                                <div class="border ${i == result.scenarios().size() - 1 ? "border-[1.5px] border-zinc-900 bg-zinc-50 shadow-sm relative" : "border-zinc-200 hover:border-zinc-300"} rounded-xl p-5 flex flex-col transition-all">
                                    @if(i == result.scenarios().size() - 1)
                                        <div class="absolute -top-2.5 right-4 bg-zinc-900 text-white text-[10px] font-bold px-2.5 py-0.5 rounded-full tracking-wider">RECOMMENDED</div>
                                    @endif
                                    
                                    <div class="text-[10px] font-bold text-zinc-500 uppercase tracking-widest mb-1.5">${scenario.scenarioType()}</div>
                                    <div class="font-bold text-zinc-900 mb-2">${scenario.scenarioTitle()}</div>
                                    <div class="text-sm text-zinc-600 mb-4 flex-1 leading-relaxed">${scenario.fitReason()}</div>
                                    
                                    <div class="pt-4 border-t ${i == result.scenarios().size() - 1 ? "border-zinc-200" : "border-zinc-100"} flex flex-col gap-2 mt-auto">
                                        <div class="flex items-center justify-between">
                                            <span class="text-xs font-semibold text-zinc-500 w-16">범위</span>
                                            <span class="text-xs font-medium text-zinc-900 text-right">${scenario.recommendedScope()}</span>
                                        </div>
                                        <div class="flex items-center justify-between">
                                            <span class="text-xs font-semibold text-zinc-500 w-16">초기 비용</span>
                                            <span class="text-xs font-bold text-zinc-900 text-right">${scenario.estimatedCostBand()}</span>
                                        </div>
                                        <div class="flex items-center justify-between">
                                            <span class="text-xs font-semibold text-zinc-500 w-16">유지비</span>
                                            <span class="text-xs font-bold text-zinc-900 text-right">${scenario.estimatedMaintenanceBand()}</span>
                                        </div>
                                        <div class="mt-2 text-xs text-warn font-semibold bg-warn/5 p-2 rounded-lg border border-warn/10">한계: ${scenario.limitations()}</div>
                                    </div>
                                </div>
                            @endfor
                        </div>
                    </div>
                @endif
                
                <!-- Additional Context -->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div class="bg-white rounded-2xl p-5 border border-zinc-200 shadow-sm">
                        <h4 class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-3">Data Quality</h4>
                        <div class="space-y-2 text-sm text-zinc-600">
                            <div class="flex justify-between"><span class="text-zinc-500">신선도:</span> <span class="font-medium text-zinc-900">${result.sampleFreshness()}</span></div>
                            <div class="flex justify-between"><span class="text-zinc-500">데이터 완전성:</span> <span class="font-medium text-zinc-900">${result.completenessScore()}/100</span></div>
                            @for(var note : result.dataQualityNotes())
                                <p class="text-xs italic text-zinc-500 mt-2">${note}</p>
                            @endfor
                        </div>
                    </div>
                    
                    <div class="bg-white rounded-2xl p-5 border border-zinc-200 shadow-sm">
                        <h4 class="text-xs font-bold text-zinc-400 uppercase tracking-widest mb-3">Links & Sources</h4>
                        <div class="space-y-3 text-sm">
                            <a href="${result.localGuidanceUrl()}" class="flex items-center text-zinc-600 hover:text-zinc-900 transition-colors">
                                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"></path></svg>
                                주정부(State) 가이드라인
                            </a>
                            <a href="${result.certifiedLabUrl()}" class="flex items-center text-zinc-600 hover:text-zinc-900 transition-colors">
                                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1"></path></svg>
                                공인 검사소 인증 정보
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Block 4: CTA -->
                @if(ctaLinks != null && !ctaLinks.isEmpty())
                    <div class="bg-zinc-900 rounded-2xl p-6 lg:p-8 text-center lg:text-left flex flex-col lg:flex-row items-center justify-between shadow-float mt-4">
                        <div class="mb-5 lg:mb-0 max-w-sm">
                            <h4 class="text-lg font-bold text-white mb-2 tracking-tight">전문가의 도움이 필요하신가요?</h4>
                            <p class="text-zinc-400 text-sm leading-relaxed">신뢰할 수 있는 지역 공인 수질 전문가를 연결하거나 파트너 할인 혜택을 확인해 보세요.</p>
                        </div>
                        <div class="flex items-center gap-3 w-full lg:w-auto flex-col sm:flex-row">
                            @for(var cta : ctaLinks)
                                @if(cta.type().contains("affiliate") || cta.type().contains("partner"))
                                    <a href="${cta.outboundUrl()}" class="w-full sm:w-auto px-5 py-3 rounded-xl font-semibold text-sm bg-white text-zinc-900 hover:bg-zinc-100 transition-all shadow-[0_0_20px_rgba(255,255,255,0.15)] ring-1 ring-white/50 inline-block text-center whitespace-nowrap">${cta.label()}</a>
                                @else
                                    <a href="${cta.outboundUrl()}" class="w-full sm:w-auto px-5 py-3 rounded-xl font-semibold text-sm bg-zinc-800 text-white hover:bg-zinc-700 transition-colors border border-zinc-700 shadow-sm inline-block text-center whitespace-nowrap">${cta.label()}</a>
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
            // Auto expand bottom sheet on mobile when viewing result
            document.addEventListener('DOMContentLoaded', () => {
                if (window.innerWidth <= 1023) {
                    const sheet = document.querySelector('.mobile-bottom-sheet');
                    if (sheet) {
                        setTimeout(() => toggleBottomSheet(), 100);
                    }
                }
            });
        </script>
    `
)
