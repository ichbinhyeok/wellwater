@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> analytes
@param java.util.List<String> qualifiers
@param java.util.List<String> states
@param java.util.List<String> useScopes
@param java.util.List<String> existingTreatments
@param java.util.List<String> reportSupportSignals
@param java.util.List<String> smellTypes
@param java.util.List<String> stainTypes
@param java.util.List<String> tasteTypes
@param java.util.List<String> locationScopes
@param java.util.List<String> changeTimings

!{var unitOptions = java.util.List.of("mg/L", "ppm", "ppb", "ug/L", "ppt", "pCi/L", "grains/gal", "su", "cfu/100ml", "presence/absence", "unknown");}

@template.layout.engine(
    title = "Result-First Well Water Tool",
    robots = "noindex,nofollow",
    description = "Enter test results and get a structured decision output.",
    activeEntryMode = request.getEntryMode(),
    leftPanel = @`
        <h2 class="text-2xl font-bold tracking-tight text-zinc-900 mb-2">Start with a lab result</h2>
        <p class="text-zinc-500 text-sm mb-8 leading-relaxed">Enter the latest analyte measurement from your lab report. The engine uses this data to route safer next actions.</p>

        <form method="post" action="/tool/result" class="space-y-5">
            <input type="hidden" name="entryMode" value="result-first">
            <input type="hidden" name="slugHint" value="${request.getSlugHint()}">

            <!-- Field 1: Analyte -->
            <div class="group relative bg-white border border-zinc-200 rounded-2xl p-5 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 focus-within:ring-zinc-900 transition-all">
                <div class="flex justify-between items-center mb-1">
                    <label class="text-sm font-semibold text-zinc-900">Analyte</label>
                </div>
                <select name="analyteName" class="w-full mt-2 text-base outline-none bg-transparent text-zinc-900 cursor-pointer">
                    <option value="">Select analyte</option>
                    @for (var analyte : analytes)
                        <option value="${analyte}" selected="${analyte.equalsIgnoreCase(request.getAnalyteName())}">${analyte}</option>
                    @endfor
                </select>
            </div>

            <!-- Field 2: Value & Unit -->
            <div class="flex gap-3">
                <div class="flex-1 group relative bg-white border border-zinc-200 rounded-2xl p-5 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 focus-within:ring-zinc-900 transition-all">
                    <label class="text-sm font-semibold text-zinc-900 mb-2 block">Value</label>
                    <input name="resultValue" value="${request.getResultValue()}" placeholder="e.g. 12" class="w-full text-xl font-medium outline-none placeholder:text-zinc-300 bg-transparent text-zinc-900">
                </div>
                <div class="w-1/3 group relative bg-white border border-zinc-200 rounded-2xl p-5 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 focus-within:ring-zinc-900 transition-all">
                    <label class="text-sm font-semibold text-zinc-900 mb-2 block">Unit</label>
                    <select name="unit" class="w-full text-base outline-none bg-transparent text-zinc-900 cursor-pointer">
                        <option value="">Select unit</option>
                        <option value="mg/L" selected="${"mg/L".equalsIgnoreCase(request.getUnit())}">mg/L</option>
                        <option value="ppm" selected="${"ppm".equalsIgnoreCase(request.getUnit())}">ppm</option>
                        <option value="ppb" selected="${"ppb".equalsIgnoreCase(request.getUnit())}">ppb</option>
                        <option value="ug/L" selected="${"ug/L".equalsIgnoreCase(request.getUnit())}">ug/L</option>
                        <option value="ppt" selected="${"ppt".equalsIgnoreCase(request.getUnit())}">ppt</option>
                        <option value="pCi/L" selected="${"pCi/L".equalsIgnoreCase(request.getUnit())}">pCi/L</option>
                        <option value="grains/gal" selected="${"grains/gal".equalsIgnoreCase(request.getUnit())}">grains/gal</option>
                        <option value="su" selected="${"su".equalsIgnoreCase(request.getUnit())}">su</option>
                        <option value="cfu/100ml" selected="${"cfu/100ml".equalsIgnoreCase(request.getUnit())}">CFU/100mL</option>
                        <option value="presence/absence" selected="${"presence/absence".equalsIgnoreCase(request.getUnit())}">presence/absence</option>
                        <option value="unknown" selected="${"unknown".equalsIgnoreCase(request.getUnit())}">unknown</option>
                    </select>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-3">
                <!-- Field 3: Qualifier -->
                <div class="group relative bg-white border border-zinc-200 rounded-2xl p-4 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 transition-all">
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Qualifier</label>
                    <select name="qualifier" class="w-full text-sm outline-none bg-transparent text-zinc-900">
                        @for (var qualifier : qualifiers)
                            <option value="${qualifier}" selected="${qualifier.equalsIgnoreCase(request.getQualifier())}">${qualifier}</option>
                        @endfor
                    </select>
                </div>

                <!-- Field 4: Qualifier Value -->
                <div class="group relative bg-white border border-zinc-200 rounded-2xl p-4 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 transition-all">
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Qualifier value</label>
                    <input name="qualifierValue" value="${request.getQualifierValue()}" placeholder="e.g. 0.005" class="w-full text-sm outline-none placeholder:text-zinc-300 bg-transparent text-zinc-900">
                </div>
            </div>

            <!-- Additional Details Grid -->
            <div class="grid grid-cols-2 gap-x-3 gap-y-4 pt-4 border-t border-zinc-100">
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Sample Date</label>
                    <input type="date" name="sampleDate" value="${request.getSampleDate()}" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Sample Source</label>
                    <select name="sampleSource" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="raw well" selected="${"raw well".equalsIgnoreCase(request.getSampleSource())}">raw well</option>
                        <option value="treated tap" selected="${"treated tap".equalsIgnoreCase(request.getSampleSource())}">treated tap</option>
                        <option value="unknown" selected="${"unknown".equalsIgnoreCase(request.getSampleSource())}">unknown</option>
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Lab Certified</label>
                    <select name="labCertified" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="yes" selected="${"yes".equalsIgnoreCase(request.getLabCertified())}">yes</option>
                        <option value="no" selected="${"no".equalsIgnoreCase(request.getLabCertified())}">no</option>
                        <option value="unknown" selected="${"unknown".equalsIgnoreCase(request.getLabCertified())}">unknown</option>
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">State</label>
                    <select name="state" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select state</option>
                        @for (var state : states)
                            <option value="${state}" selected="${state.equalsIgnoreCase(request.getState())}">${state}</option>
                        @endfor
                    </select>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-3">
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Use Scope</label>
                    <select name="useScope" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select scope</option>
                        @for (var scope : useScopes)
                            <option value="${scope}" selected="${scope.equalsIgnoreCase(request.getUseScope())}">${scope}</option>
                        @endfor
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Household Size</label>
                    <input name="householdSize" value="${request.getHouseholdSize()}" placeholder="e.g. 4" class="w-full text-sm outline-none placeholder:text-zinc-300 bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                </div>
            </div>

            <div class="grid grid-cols-2 gap-3">
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Lab Name</label>
                    <input name="labName" value="${request.getLabName()}" placeholder="optional lab name" class="w-full text-sm outline-none placeholder:text-zinc-300 bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Issue Location</label>
                    <select name="locationScope" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select location</option>
                        @for (var option : locationScopes)
                            <option value="${option}" selected="${option.equalsIgnoreCase(request.getLocationScope())}">${option}</option>
                        @endfor
                    </select>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-3">
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Change Timing</label>
                    <select name="changeTiming" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select timing</option>
                        @for (var option : changeTimings)
                            <option value="${option}" selected="${option.equalsIgnoreCase(request.getChangeTiming())}">${option}</option>
                        @endfor
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Taste</label>
                    <select name="tasteType" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select taste</option>
                        @for (var option : tasteTypes)
                            <option value="${option}" selected="${option.equalsIgnoreCase(request.getTasteType())}">${option}</option>
                        @endfor
                    </select>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-3">
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Smell</label>
                    <select name="smellType" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select smell</option>
                        @for (var option : smellTypes)
                            <option value="${option}" selected="${option.equalsIgnoreCase(request.getSmellType())}">${option}</option>
                        @endfor
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Stain</label>
                    <select name="stainType" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select stain</option>
                        @for (var option : stainTypes)
                            <option value="${option}" selected="${option.equalsIgnoreCase(request.getStainType())}">${option}</option>
                        @endfor
                    </select>
                </div>
            </div>

            <div class="bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                <p class="text-xs font-bold text-zinc-900 mb-1 tracking-wide uppercase">Existing Treatment</p>
                <p class="text-xs text-zinc-500 mb-3">Check all that apply. Leave blank if none.</p>
                <div class="grid grid-cols-2 gap-2">
                    @for (var treatment : existingTreatments)
                        <label class="flex items-center gap-2 text-sm text-zinc-700">
                            <input type="checkbox" name="existingTreatments" value="${treatment}" checked="${request.hasExistingTreatment(treatment)}" class="w-4 h-4 rounded text-zinc-900 focus:ring-zinc-900 border-zinc-300">
                            <span>${treatment}</span>
                        </label>
                    @endfor
                </div>
            </div>

            <div class="bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                <p class="text-xs font-bold text-zinc-900 mb-1 tracking-wide uppercase">Companion Report Lines</p>
                <p class="text-xs text-zinc-500 mb-3">Optional. Add other lines from the same lab report so the engine can read surrounding context before it recommends testing or compare paths.</p>
                !{var companionLines = request.getCompanionLinesForView();}
                <div id="companion-lines" data-next-index="${companionLines.size()}" class="space-y-3">
                    @for (int i = 0; i < companionLines.size(); i++)
                        !{var companionLine = companionLines.get(i);}
                        !{var fieldPrefix = "companionLines[" + i + "]";}
                        <div data-companion-row class="rounded-xl border border-zinc-200 bg-white p-3">
                            <div class="flex items-center justify-between gap-3 mb-3">
                                <p class="text-xs font-semibold uppercase tracking-wide text-zinc-500">Report line ${i + 1}</p>
                                <button type="button" onclick="removeCompanionLineRow(this)" class="text-xs font-semibold text-zinc-500 hover:text-zinc-900 transition-colors">Remove</button>
                            </div>
                            <div class="grid grid-cols-1 md:grid-cols-5 gap-2">
                                <select name="${fieldPrefix}.analyteName" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                                    <option value="">Companion analyte</option>
                                    @for (var analyte : analytes)
                                        <option value="${analyte}" selected="${analyte.equalsIgnoreCase(companionLine.getAnalyteName())}">${analyte}</option>
                                    @endfor
                                </select>
                                <input name="${fieldPrefix}.resultValue" value="${companionLine.getResultValue()}" placeholder="Value" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                                <select name="${fieldPrefix}.unit" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                                    <option value="">Unit</option>
                                    @for (var unitOption : unitOptions)
                                        <option value="${unitOption}" selected="${unitOption.equalsIgnoreCase(companionLine.getUnit())}">${unitOption.equals("cfu/100ml") ? "CFU/100mL" : unitOption}</option>
                                    @endfor
                                </select>
                                <select name="${fieldPrefix}.qualifier" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                                    @for (var qualifier : qualifiers)
                                        <option value="${qualifier}" selected="${qualifier.equalsIgnoreCase(companionLine.getQualifier())}">${qualifier}</option>
                                    @endfor
                                </select>
                                <input name="${fieldPrefix}.qualifierValue" value="${companionLine.getQualifierValue()}" placeholder="Qualifier value" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                            </div>
                        </div>
                    @endfor
                </div>
                <button type="button" onclick="addCompanionLineRow()" class="mt-3 inline-flex items-center justify-center rounded-lg border border-zinc-300 bg-white px-3 py-2 text-sm font-semibold text-zinc-900 hover:border-zinc-900 transition-colors">
                    Add report line
                </button>
            </div>

            <div class="bg-zinc-50 p-4 rounded-xl border border-zinc-200">
                <p class="text-xs font-bold text-zinc-900 mb-1 tracking-wide uppercase">Other Context Flags</p>
                <p class="text-xs text-zinc-500 mb-3">Optional. Mark event or symptom context that matters even if it is not a clean analyte line from the report.</p>
                <div class="grid grid-cols-2 gap-2">
                    @for (var signal : reportSupportSignals)
                        <label class="flex items-center gap-2 text-sm text-zinc-700">
                            <input type="checkbox" name="supportingSignals" value="${signal}" checked="${request.hasSupportingSignal(signal)}" class="w-4 h-4 rounded text-zinc-900 focus:ring-zinc-900 border-zinc-300">
                            <span>${signal}</span>
                        </label>
                    @endfor
                </div>
            </div>

            <!-- Vulnerable Household Flags -->
            <div class="bg-zinc-50 p-4 rounded-xl border border-zinc-200 mt-4">
                <p class="text-xs font-bold text-zinc-900 mb-3 tracking-wide uppercase">Household Vulnerability</p>
                <div class="space-y-3">
                    <label class="flex items-center gap-3 cursor-pointer group">
                        <input type="checkbox" name="infantPresent" value="true" checked="${request.isInfantPresent()}" class="w-4 h-4 rounded text-zinc-900 focus:ring-zinc-900 border-zinc-300 cursor-pointer">
                        <span class="text-sm font-medium text-zinc-600 group-hover:text-zinc-900 transition-colors">Infant present in household</span>
                    </label>
                    <label class="flex items-center gap-3 cursor-pointer group">
                        <input type="checkbox" name="pregnancyPresent" value="true" checked="${request.isPregnancyPresent()}" class="w-4 h-4 rounded text-zinc-900 focus:ring-zinc-900 border-zinc-300 cursor-pointer">
                        <span class="text-sm font-medium text-zinc-600 group-hover:text-zinc-900 transition-colors">Pregnancy in household</span>
                    </label>
                    <label class="flex items-center gap-3 cursor-pointer group">
                        <input type="checkbox" name="immunocompromisedPresent" value="true" checked="${request.isImmunocompromisedPresent()}" class="w-4 h-4 rounded text-zinc-900 focus:ring-zinc-900 border-zinc-300 cursor-pointer">
                        <span class="text-sm font-medium text-zinc-600 group-hover:text-zinc-900 transition-colors">Immunocompromised person in household</span>
                    </label>
                </div>
            </div>

            <button type="submit" class="mt-8 flex items-center justify-center w-full py-4 rounded-xl text-base font-semibold text-white bg-zinc-900 hover:bg-zinc-800 transition-colors shadow-sm">
                Run Decision Engine
            </button>
        </form>

        <template id="companion-line-template">
            <div data-companion-row class="rounded-xl border border-zinc-200 bg-white p-3">
                <div class="flex items-center justify-between gap-3 mb-3">
                    <p class="text-xs font-semibold uppercase tracking-wide text-zinc-500">Report line</p>
                    <button type="button" onclick="removeCompanionLineRow(this)" class="text-xs font-semibold text-zinc-500 hover:text-zinc-900 transition-colors">Remove</button>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-5 gap-2">
                    <select name="companionLines[__INDEX__].analyteName" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                        <option value="">Companion analyte</option>
                        @for (var analyte : analytes)
                            <option value="${analyte}">${analyte}</option>
                        @endfor
                    </select>
                    <input name="companionLines[__INDEX__].resultValue" placeholder="Value" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                    <select name="companionLines[__INDEX__].unit" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                        <option value="">Unit</option>
                        @for (var unitOption : unitOptions)
                            <option value="${unitOption}">${unitOption.equals("cfu/100ml") ? "CFU/100mL" : unitOption}</option>
                        @endfor
                    </select>
                    <select name="companionLines[__INDEX__].qualifier" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                        @for (var qualifier : qualifiers)
                            <option value="${qualifier}" selected="${"none".equalsIgnoreCase(qualifier)}">${qualifier}</option>
                        @endfor
                    </select>
                    <input name="companionLines[__INDEX__].qualifierValue" placeholder="Qualifier value" class="rounded-lg border border-zinc-200 bg-white px-3 py-2 text-sm text-zinc-900">
                </div>
            </div>
        </template>

        <script>
            function addCompanionLineRow() {
                const container = document.getElementById('companion-lines');
                const template = document.getElementById('companion-line-template');
                if (!container || !template) {
                    return;
                }
                const index = Number(container.dataset.nextIndex || '0');
                const markup = template.innerHTML.replace(/__INDEX__/g, String(index));
                container.insertAdjacentHTML('beforeend', markup);
                container.dataset.nextIndex = String(index + 1);
            }

            function removeCompanionLineRow(button) {
                const container = document.getElementById('companion-lines');
                const row = button.closest('[data-companion-row]');
                if (!container || !row) {
                    return;
                }
                row.remove();
                if (!container.querySelector('[data-companion-row]')) {
                    addCompanionLineRow();
                }
            }
        </script>
    `,
    rightPanel = @`
        <div class="flex-1 flex flex-col items-center justify-center text-center px-10 h-[calc(100vh-80px)] lg:h-full">
            <div class="w-20 h-20 bg-zinc-100 rounded-full flex items-center justify-center mb-6 shadow-subtle ring-1 ring-zinc-200">
                <svg class="w-8 h-8 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
            </div>
            <h3 class="text-2xl font-bold text-zinc-900 mb-2">Awaiting input</h3>
            <p class="text-zinc-500 text-sm max-w-[280px] leading-relaxed">
                Add the main analyte and any supporting report flags so the engine can separate testing, interpretation, and compare readiness more cleanly.
            </p>
        </div>
    `
)
