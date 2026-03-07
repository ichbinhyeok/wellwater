@param com.example.wellwater.web.tool.ToolRequest request
@param java.util.List<String> analytes
@param java.util.List<String> qualifiers
@param java.util.List<String> states
@param java.util.List<String> useScopes
@param java.util.List<String> existingTreatments

@template.layout.engine(
    title = "Result-First Well Water Tool",
    robots = "index,follow",
    description = "Enter test results and get a structured decision output.",
    activeEntryMode = request.getEntryMode(),
    leftPanel = @`
        <h2 class="text-2xl font-bold tracking-tight text-zinc-900 mb-2">물 성분 분석표 입력</h2>
        <p class="text-zinc-500 text-sm mb-8 leading-relaxed">연구소에서 받은 수질 검사 결과의 주요 수치를 입력해주세요. 엔진이 실시간으로 안전 여부를 판정합니다.</p>
        
        <form method="post" action="/tool/result" class="space-y-5">
            <input type="hidden" name="entryMode" value="result-first">
            <input type="hidden" name="slugHint" value="${request.getSlugHint()}">

            <!-- Field 1: Analyte -->
            <div class="group relative bg-white border border-zinc-200 rounded-2xl p-5 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 focus-within:ring-zinc-900 transition-all">
                <div class="flex justify-between items-center mb-1">
                    <label class="text-sm font-semibold text-zinc-900">Analyte (성분명)</label>
                </div>
                <select name="analyteName" class="w-full mt-2 text-base outline-none bg-transparent text-zinc-900 cursor-pointer">
                    <option value="">Select analyte</option>
                    @for (var analyte : analytes)
                        <option value="${analyte}" @if (analyte.equalsIgnoreCase(request.getAnalyteName()))selected@end>${analyte}</option>
                    @endfor
                </select>
            </div>

            <!-- Field 2: Value & Unit -->
            <div class="flex gap-3">
                <div class="flex-1 group relative bg-white border border-zinc-200 rounded-2xl p-5 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 focus-within:ring-zinc-900 transition-all">
                    <label class="text-sm font-semibold text-zinc-900 mb-2 block">Value (수치)</label>
                    <input name="resultValue" value="${request.getResultValue()}" placeholder="e.g. 12" class="w-full text-xl font-medium outline-none placeholder:text-zinc-300 bg-transparent text-zinc-900">
                </div>
                <div class="w-1/3 group relative bg-white border border-zinc-200 rounded-2xl p-5 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 focus-within:ring-zinc-900 transition-all">
                    <label class="text-sm font-semibold text-zinc-900 mb-2 block">Unit</label>
                    <select name="unit" class="w-full text-base outline-none bg-transparent text-zinc-900 cursor-pointer">
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
                </div>
            </div>

            <div class="grid grid-cols-2 gap-3">
                <!-- Field 3: Qualifier -->
                <div class="group relative bg-white border border-zinc-200 rounded-2xl p-4 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 transition-all">
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Qualifier</label>
                    <select name="qualifier" class="w-full text-sm outline-none bg-transparent text-zinc-900">
                        @for (var qualifier : qualifiers)
                            <option value="${qualifier}" @if (qualifier.equalsIgnoreCase(request.getQualifier()))selected@end>${qualifier}</option>
                        @endfor
                    </select>
                </div>
                
                <!-- Field 4: Qualifier Value -->
                <div class="group relative bg-white border border-zinc-200 rounded-2xl p-4 hover:border-zinc-300 focus-within:border-zinc-900 focus-within:ring-1 transition-all">
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Qualifier Value</label>
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
                        <option value="raw well" @if ("raw well".equalsIgnoreCase(request.getSampleSource()))selected@end>raw well</option>
                        <option value="treated tap" @if ("treated tap".equalsIgnoreCase(request.getSampleSource()))selected@end>treated tap</option>
                        <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getSampleSource()))selected@end>unknown</option>
                    </select>
                </div>
                <div>
                    <label class="text-xs font-semibold text-zinc-500 mb-1 block">Lab Certified</label>
                    <select name="labCertified" class="w-full text-sm outline-none bg-transparent text-zinc-900 border border-zinc-200 rounded-lg p-2 focus:border-zinc-900">
                        <option value="yes" @if ("yes".equalsIgnoreCase(request.getLabCertified()))selected@end>yes</option>
                        <option value="no" @if ("no".equalsIgnoreCase(request.getLabCertified()))selected@end>no</option>
                        <option value="unknown" @if ("unknown".equalsIgnoreCase(request.getLabCertified()))selected@end>unknown</option>
                    </select>
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
                <svg class="w-8 h-8 text-zinc-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
            </div>
            <h3 class="text-2xl font-bold text-zinc-900 mb-2">대기 중 (Awaiting Input)</h3>
            <p class="text-zinc-500 text-sm max-w-[280px] leading-relaxed">
                좌측 패널에 분석 결과 수치를 입력하시면, 안전 규정을 바탕으로 즉각적인 판단과 행동 지침이 이 영역에 표시됩니다.
            </p>
        </div>
    `
)
