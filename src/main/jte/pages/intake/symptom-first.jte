@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> symptoms
@param java.util.List<String> states
@param java.util.List<String> useScopes
@param java.util.List<String> existingTreatments
@param java.util.List<String> smellTypes
@param java.util.List<String> stainTypes
@param java.util.List<String> tasteTypes
@param java.util.List<String> locationScopes
@param java.util.List<String> changeTimings
@param String googleAnalyticsId

@template.layout.engine(
    title = "Symptom-First Well Water Tool",
    robots = "noindex,nofollow",
    description = "Start from symptom and triage into a decision path.",
    activeEntryMode = request.getEntryMode(),
    googleAnalyticsId = googleAnalyticsId,
    leftPanel = @`
        <h2 class="text-2xl font-bold tracking-tight text-zinc-900 mb-2">Start with a symptom</h2>
        <p class="text-zinc-500 text-sm mb-8 leading-relaxed">Choose the main issue you are observing. The engine will triage likely causes and propose safer next actions.</p>

        <form method="post" action="/tool/result" class="space-y-5">
            <input type="hidden" name="entryMode" value="symptom-first">
            <input type="hidden" name="slugHint" value="${request.getSlugHint()}">

            <!-- Primary Symptom -->
            <div class="group relative bg-white border border-zinc-200 rounded-2xl p-5 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 focus-within:ring-zinc-900 transition-all">
                <div class="flex justify-between items-center mb-1">
                    <label class="text-sm font-semibold text-zinc-900">Primary Symptom</label>
                </div>
                <select name="symptomFlag" class="w-full mt-2 text-base outline-none bg-transparent text-zinc-900 cursor-pointer">
                    <option value="">Select symptom</option>
                    @for (var symptom : symptoms)
                        <option value="${symptom}" selected="${symptom.equalsIgnoreCase(request.getSymptomFlag())}">${symptom}</option>
                    @endfor
                </select>
            </div>

            <!-- Additional Details Grid -->
            <div class="grid grid-cols-2 gap-x-3 gap-y-4 pt-4 border-t border-zinc-100">
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Sample Source</label>
                    <select name="sampleSource" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="unknown" selected="${"unknown".equalsIgnoreCase(request.getSampleSource())}">unknown</option>
                        <option value="raw well" selected="${"raw well".equalsIgnoreCase(request.getSampleSource())}">raw well</option>
                        <option value="treated tap" selected="${"treated tap".equalsIgnoreCase(request.getSampleSource())}">treated tap</option>
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Lab Certified</label>
                    <select name="labCertified" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="unknown" selected="${"unknown".equalsIgnoreCase(request.getLabCertified())}">unknown</option>
                        <option value="yes" selected="${"yes".equalsIgnoreCase(request.getLabCertified())}">yes</option>
                        <option value="no" selected="${"no".equalsIgnoreCase(request.getLabCertified())}">no</option>
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Sample Date (optional)</label>
                    <input type="date" name="sampleDate" value="${request.getSampleDate()}" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
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
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Issue Location</label>
                    <select name="locationScope" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select location</option>
                        @for (var option : locationScopes)
                            <option value="${option}" selected="${option.equalsIgnoreCase(request.getLocationScope())}">${option}</option>
                        @endfor
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Change Timing</label>
                    <select name="changeTiming" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="">Select timing</option>
                        @for (var option : changeTimings)
                            <option value="${option}" selected="${option.equalsIgnoreCase(request.getChangeTiming())}">${option}</option>
                        @endfor
                    </select>
                </div>
            </div>

            <div class="grid grid-cols-3 gap-3">
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
    `,
    rightPanel = @`
        <div class="flex-1 flex flex-col items-center justify-center text-center px-10 h-[calc(100vh-80px)] lg:h-full">
            <div class="w-20 h-20 bg-zinc-100 rounded-full flex items-center justify-center mb-6 shadow-subtle ring-1 ring-zinc-200">
                <svg class="w-8 h-8 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.001 4.001 0 003 15z"></path></svg>
            </div>
            <h3 class="text-2xl font-bold text-zinc-900 mb-2">Awaiting input</h3>
            <p class="text-zinc-500 text-sm max-w-[280px] leading-relaxed">
                Choose the symptom and add context to receive a likely cause category and a practical action path.
            </p>
        </div>
    `
)
