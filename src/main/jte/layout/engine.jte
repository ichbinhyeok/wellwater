@param String title
@param String robots
@param String description
@param String activeEntryMode
@param gg.jte.Content leftPanel
@param gg.jte.Content rightPanel

<!DOCTYPE html>
<html lang="en" class="antialiased text-zinc-900 bg-zinc-50">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="robots" content="${robots}">
    <meta name="description" content="${description}">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: { sans: ['Inter', 'sans-serif'] },
                    colors: {
                        zinc: {
                            50: '#fafafa', 100: '#f4f4f5', 200: '#e4e4e7', 300: '#d4d4d8', 400: '#a1a1aa', 500: '#71717a', 600: '#52525b', 700: '#3f3f46', 800: '#27272a', 900: '#18181b',
                        }, safe: '#10b981', warn: '#f59e0b', danger: '#ef4444',
                    },
                    boxShadow: {
                        'subtle': '0 4px 20px -2px rgba(0, 0, 0, 0.05)',
                        'float': '0 8px 30px rgba(0, 0, 0, 0.12)',
                    }
                }
            }
        }
    </script>
    <style>
        ::-webkit-scrollbar { width: 6px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb { background: #d4d4d8; border-radius: 4px; }
        ::-webkit-scrollbar-thumb:hover { background: #a1a1aa; }
        .glass { background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px); border: 1px solid rgba(255, 255, 255, 0.3); }
        .no-scrollbar::-webkit-scrollbar { display: none; }
        .no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
        @media (max-width: 1023px) {
            .mobile-bottom-sheet {
                position: fixed; bottom: 0; left: 0; right: 0; z-index: 50;
                border-top-left-radius: 24px; border-top-right-radius: 24px;
                box-shadow: 0 -10px 40px rgba(0,0,0,0.1); max-height: 85vh;
                transform: translateY(calc(100% - 80px)); transition: transform 0.4s cubic-bezier(0.32, 0.72, 0, 1);
            }
            .mobile-bottom-sheet.expanded { transform: translateY(0); }
        }
    </style>
</head>
<body class="bg-zinc-50 h-screen w-full overflow-hidden flex flex-col lg:flex-row">

    <!-- Left Panel: Input / Triage (50%) -->
    <section class="w-full lg:w-1/2 h-full flex flex-col relative z-30 bg-white border-r border-zinc-200 overflow-y-auto no-scrollbar shadow-[4px_0_24px_rgba(0,0,0,0.02)] pt-6 lg:pt-10 px-6 lg:px-12 xl:px-20">
        <!-- Header -->
        <header class="mb-8 flex items-center justify-between">
            <div class="flex items-center gap-3">
                <a href="/" class="w-8 h-8 rounded-lg bg-zinc-900 flex items-center justify-center hover:bg-zinc-800 transition-colors">
                    <svg class="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"></path></svg>
                </a>
                <h1 class="text-xl font-semibold tracking-tight text-zinc-900">Well Water Verdict</h1>
            </div>
            <a href="/tool/${activeEntryMode}" class="text-sm font-medium text-zinc-500 hover:text-zinc-900 transition-colors">Reset</a>
        </header>

        <!-- Segmented Control -->
        <div class="bg-zinc-100 p-1 rounded-xl flex mb-10">
            <a href="/tool/result-first" class="flex-1 py-2.5 text-center text-sm font-medium ${"result-first".equals(activeEntryMode) ? "text-zinc-900 bg-white rounded-lg shadow-sm ring-1 ring-zinc-900/5" : "text-zinc-500 hover:text-zinc-700"} transition-all">진단서 입력</a>
            <a href="/tool/symptom-first" class="flex-1 py-2.5 text-center text-sm font-medium ${"symptom-first".equals(activeEntryMode) ? "text-zinc-900 bg-white rounded-lg shadow-sm ring-1 ring-zinc-900/5" : "text-zinc-500 hover:text-zinc-700"} transition-all">증상 선택</a>
            <a href="/tool/trigger-first" class="flex-1 py-2.5 text-center text-sm font-medium ${"trigger-first".equals(activeEntryMode) ? "text-zinc-900 bg-white rounded-lg shadow-sm ring-1 ring-zinc-900/5" : "text-zinc-500 hover:text-zinc-700"} transition-all">사건 발생</a>
        </div>

        <div class="flex-1 pb-32 lg:pb-12">
            ${leftPanel}
        </div>
    </section>

    <!-- Right Panel: Output / Verdict (50%) -->
    <section class="w-full lg:w-1/2 h-full flex flex-col bg-zinc-50 mobile-bottom-sheet lg:relative lg:transform-none lg:z-0 py-0 lg:py-10 px-0 lg:px-12 xl:px-20 overflow-visible lg:overflow-y-auto no-scrollbar border-l border-zinc-200/50">
        ${rightPanel}
    </section>

    <script>
        function toggleBottomSheet() {
            const sheet = document.querySelector('.mobile-bottom-sheet');
            const icon = document.getElementById('chevron-icon');
            const summary = document.getElementById('mobile-summary');
            if(!sheet) return;
            sheet.classList.toggle('expanded');
            
            if (sheet.classList.contains('expanded')) {
                if(icon) icon.classList.remove('rotate-180');
                if(summary) {
                    summary.classList.add('opacity-0');
                    setTimeout(() => summary.classList.add('hidden'), 300);
                }
            } else {
                if(icon) icon.classList.add('rotate-180');
                if(summary) {
                    summary.classList.remove('hidden');
                    setTimeout(() => summary.classList.remove('opacity-0'), 10);
                }
            }
        }
        
        // Ensure bottom sheet auto-expands on mobile if result exists (via a script in view.jte)
    </script>
</body>
</html>
