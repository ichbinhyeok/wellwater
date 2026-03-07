@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> symptoms
@param java.util.List<String> states
@param java.util.List<String> useScopes
@param java.util.List<String> existingTreatments

@template.layout.engine(
    title = "Symptom-First Well Water Tool",
    robots = "index,follow",
    description = "Start from symptom and triage into a decision path.",
    activeEntryMode = request.getEntryMode(),
    leftPanel = @`
        <h2 class="text-2xl font-bold tracking-tight text-zinc-900 mb-2">물 상태(증상) 입력</h2>
        <p class="text-zinc-500 text-sm mb-8 leading-relaxed">수질에서 관찰하거나 느끼신 증상을 선택해주세요. 엔진이 심각도를 판별하고 즉각적인 조치를 제안합니다.</p>
        
        <form method="post" action="/tool/result" class="space-y-5">
            <input type="hidden" name="entryMode" value="symptom-first">
            <input type="hidden" name="slugHint" value="${request.getSlugHint()}">

            <!-- Primary Symptom -->
            <div class="group relative bg-white border border-zinc-200 rounded-2xl p-5 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 focus-within:ring-zinc-900 transition-all">
                <div class="flex justify-between items-center mb-1">
                    <label class="text-sm font-semibold text-zinc-900">Primary Symptom (증상)</label>
                </div>
                <select name="symptomFlag" class="w-full mt-2 text-base outline-none bg-transparent text-zinc-900 cursor-pointer">
                    <option value="">Select symptom</option>
                    @for (var symptom : symptoms)
                        <option value="${symptom}" @if (symptom.equalsIgnoreCase(request.getSymptomFlag()))selected@end>${symptom}</option>
                    @endfor
                </select>
            </div>

            <!-- Additional Details Grid -->
            <div class="grid grid-cols-2 gap-x-3 gap-y-4 pt-4 border-t border-zinc-100">
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Sample Source</label>
                    <select name="sampleSource" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getSampleSource()))selected@end>unknown</option>
                        <option value="raw well" @if ("raw well".equalsIgnoreCase(request.getSampleSource()))selected@end>raw well</option>
                        <option value="treated tap" @if ("treated tap".equalsIgnoreCase(request.getSampleSource()))selected@end>treated tap</option>
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Lab Certified</label>
                    <select name="labCertified" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getLabCertified()))selected@end>unknown</option>
                        <option value="yes" @if ("yes".equalsIgnoreCase(request.getLabCertified()))selected@end>yes</option>
                        <option value="no" @if ("no".equalsIgnoreCase(request.getLabCertified()))selected@end>no</option>
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
                            <option value="${state}" @if (state.equalsIgnoreCase(request.getState()))selected@end>${state}</option>
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
                            <option value="${scope}" @if (scope.equalsIgnoreCase(request.getUseScope()))selected@end>${scope}</option>
                        @endfor
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Existing Treatment</label>
                    <select name="existingTreatment" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        @for (var treatment : existingTreatments)
                            <option value="${treatment}" @if (treatment.equalsIgnoreCase(request.getExistingTreatment()))selected@end>${treatment}</option>
                        @endfor
                    </select>
                </div>
            </div>

            <!-- Vulnerable Household Flags -->
            <div class="bg-zinc-50 p-4 rounded-xl border border-zinc-200 mt-4">
                <p class="text-xs font-bold text-zinc-900 mb-3 tracking-wide uppercase">Household Vulnerability</p>
                <div class="space-y-3">
                    <label class="flex items-center gap-3 cursor-pointer group">
                        <input type="checkbox" name="infantPresent" value="true" @if (request.isInfantPresent())checked@end class="w-4 h-4 rounded text-zinc-900 focus:ring-zinc-900 border-zinc-300 cursor-pointer">
                        <span class="text-sm font-medium text-zinc-600 group-hover:text-zinc-900 transition-colors">Infant present (영유아 거주)</span>
                    </label>
                    <label class="flex items-center gap-3 cursor-pointer group">
                        <input type="checkbox" name="pregnancyPresent" value="true" @if (request.isPregnancyPresent())checked@end class="w-4 h-4 rounded text-zinc-900 focus:ring-zinc-900 border-zinc-300 cursor-pointer">
                        <span class="text-sm font-medium text-zinc-600 group-hover:text-zinc-900 transition-colors">Pregnancy present (임산부 거주)</span>
                    </label>
                    <label class="flex items-center gap-3 cursor-pointer group">
                        <input type="checkbox" name="immunocompromisedPresent" value="true" @if (request.isImmunocompromisedPresent())checked@end class="w-4 h-4 rounded text-zinc-900 focus:ring-zinc-900 border-zinc-300 cursor-pointer">
                        <span class="text-sm font-medium text-zinc-600 group-hover:text-zinc-900 transition-colors">Immunocompromised present (면역 저하자)</span>
                    </label>
                </div>
            </div>

            <button type="submit" class="mt-8 flex items-center justify-center w-full py-4 rounded-xl text-base font-semibold text-white bg-zinc-900 hover:bg-zinc-800 transition-colors shadow-sm">
                Run Decision Engine ➔
            </button>
        </form>
    `,
    rightPanel = @`
        <div class="flex-1 flex flex-col items-center justify-center text-center px-10 h-[calc(100vh-80px)] lg:h-full">
            <div class="w-20 h-20 bg-zinc-100 rounded-full flex items-center justify-center mb-6 shadow-subtle ring-1 ring-zinc-200">
                <svg class="w-8 h-8 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.001 4.001 0 003 15z"></path></svg>
            </div>
            <h3 class="text-2xl font-bold text-zinc-900 mb-2">대기 중 (Awaiting Input)</h3>
            <p class="text-zinc-500 text-sm max-w-[280px] leading-relaxed">
                좌측 패널에 판단이 필요한 증상을 선택하시면, 엔진이 실시간으로 확인 후 원인과 행동 지침을 이 영역에 표시합니다.
            </p>
        </div>
    `
)
