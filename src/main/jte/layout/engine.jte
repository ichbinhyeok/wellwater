@param String title
@param String robots
@param String description
@param String activeEntryMode
@param gg.jte.Content leftPanel
@param gg.jte.Content rightPanel

<!DOCTYPE html>
<html lang="en" class="antialiased">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="robots" content="${robots}">
    <meta name="description" content="${description}">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Sora:wght@500;600;700&display=swap" rel="stylesheet">
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: {
                        sans: ['Manrope', 'sans-serif'],
                        display: ['Sora', 'sans-serif']
                    },
                    colors: {
                        zinc: {
                            50: '#f6f7f3', 100: '#edf0e7', 200: '#d9e0d0', 300: '#b8c4b0',
                            400: '#879488', 500: '#5f6f63', 600: '#405144', 700: '#2d3b31',
                            800: '#1d271f', 900: '#121914'
                        },
                        safe: '#1f9d73',
                        warn: '#d8862d',
                        danger: '#d2553f',
                        accent: '#0f766e',
                        accentdeep: '#154d57'
                    },
                    boxShadow: {
                        'subtle': '0 16px 40px rgba(25, 42, 28, 0.08)',
                        'float': '0 24px 60px rgba(25, 42, 28, 0.14)'
                    }
                }
            }
        }
    </script>
    <style>
        body {
            background:
                radial-gradient(circle at top left, rgba(15, 118, 110, 0.16), transparent 28%),
                radial-gradient(circle at top right, rgba(216, 134, 45, 0.14), transparent 26%),
                linear-gradient(180deg, #eef2ea 0%, #f4f6f2 48%, #f8f8f4 100%);
        }
        ::-webkit-scrollbar { width: 8px; height: 8px; }
        ::-webkit-scrollbar-track { background: transparent; }
        ::-webkit-scrollbar-thumb { background: rgba(95, 111, 99, 0.35); border-radius: 999px; }
        ::-webkit-scrollbar-thumb:hover { background: rgba(95, 111, 99, 0.55); }
        .no-scrollbar::-webkit-scrollbar { display: none; }
        .no-scrollbar { -ms-overflow-style: none; scrollbar-width: none; }
        .engine-frame {
            position: relative;
            z-index: 1;
        }
        .engine-shell {
            background: rgba(255, 255, 255, 0.55);
            border: 1px solid rgba(255, 255, 255, 0.75);
            backdrop-filter: blur(20px);
        }
        .engine-left {
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.92), rgba(250, 251, 247, 0.88));
            box-shadow: inset -1px 0 0 rgba(20, 34, 24, 0.06);
        }
        .engine-right {
            background:
                linear-gradient(180deg, rgba(244, 247, 241, 0.88), rgba(250, 250, 247, 0.78));
        }
        .engine-nav {
            background: rgba(18, 25, 20, 0.04);
            border: 1px solid rgba(18, 25, 20, 0.06);
        }
        .engine-kicker {
            letter-spacing: 0.14em;
            text-transform: uppercase;
        }
        .engine-brand {
            box-shadow: 0 16px 30px rgba(15, 118, 110, 0.22);
        }
        @media (max-width: 1023px) {
            body {
                overflow-y: auto;
            }
            .engine-frame {
                height: auto;
                min-height: 100vh;
                padding: 10px;
            }
            .engine-shell {
                height: auto;
                min-height: calc(100vh - 20px);
                overflow: visible;
            }
            .engine-left,
            .engine-right {
                width: 100%;
                height: auto;
                overflow: visible;
            }
            .engine-left {
                box-shadow: none;
                padding-bottom: 28px;
            }
            .engine-right {
                border-top: 1px solid rgba(18, 25, 20, 0.08);
            }
            .mobile-bottom-sheet {
                position: relative;
                bottom: auto;
                left: auto;
                right: auto;
                z-index: auto;
                border-top-left-radius: 0;
                border-top-right-radius: 0;
                box-shadow: none;
                max-height: none;
                transform: none;
                transition: none;
            }
            .mobile-bottom-sheet.expanded { transform: none; }
        }
    </style>
</head>
<body class="min-h-screen w-full overflow-hidden text-zinc-900">
    <div class="engine-frame w-full px-3 py-3 lg:px-5 lg:py-5">
        <div class="engine-shell min-h-[calc(100vh-24px)] lg:min-h-0 rounded-[28px] lg:rounded-[34px] overflow-hidden flex flex-col lg:flex-row shadow-float">
            <section class="engine-left w-full lg:w-1/2 flex flex-col relative z-30 overflow-visible lg:overflow-y-auto no-scrollbar pt-6 lg:pt-8 px-6 lg:px-10 xl:px-14">
                <header class="mb-8 flex items-center justify-between gap-4">
                    <div class="flex items-center gap-4 min-w-0">
                        <a href="/" class="engine-brand w-11 h-11 rounded-2xl bg-gradient-to-br from-accent to-accentdeep text-white flex items-center justify-center font-display font-semibold tracking-tight">
                            WW
                        </a>
                        <div class="min-w-0">
                            <p class="engine-kicker text-[11px] font-bold text-zinc-500 mb-1">Decision Engine</p>
                            <h1 class="font-display text-xl font-semibold tracking-tight text-zinc-900">Well Water Verdict</h1>
                        </div>
                    </div>
                    <a href="/tool/${activeEntryMode}" class="text-sm font-semibold text-zinc-500 hover:text-zinc-900 transition-colors">Reset</a>
                </header>

                <div class="engine-nav p-1 rounded-2xl flex mb-8">
                    <a href="/tool/result-first" class="flex-1 py-2.5 text-center text-sm font-semibold ${"result-first".equals(activeEntryMode) ? "text-zinc-900 bg-white rounded-2xl shadow-sm" : "text-zinc-500 hover:text-zinc-800"} transition-all">Lab Results</a>
                    <a href="/tool/symptom-first" class="flex-1 py-2.5 text-center text-sm font-semibold ${"symptom-first".equals(activeEntryMode) ? "text-zinc-900 bg-white rounded-2xl shadow-sm" : "text-zinc-500 hover:text-zinc-800"} transition-all">Symptoms</a>
                    <a href="/tool/trigger-first" class="flex-1 py-2.5 text-center text-sm font-semibold ${"trigger-first".equals(activeEntryMode) ? "text-zinc-900 bg-white rounded-2xl shadow-sm" : "text-zinc-500 hover:text-zinc-800"} transition-all">Recent Triggers</a>
                </div>

                <div class="flex-1 pb-32 lg:pb-12">
                    ${leftPanel}
                </div>
            </section>

            <section class="engine-right w-full lg:w-1/2 flex flex-col mobile-bottom-sheet lg:relative lg:transform-none lg:z-0 py-0 lg:py-8 px-0 lg:px-10 xl:px-14 overflow-visible lg:overflow-y-auto no-scrollbar">
                ${rightPanel}
            </section>
        </div>
    </div>

    <script>
        function toggleBottomSheet() {
            const sheet = document.querySelector('.mobile-bottom-sheet');
            const icon = document.getElementById('chevron-icon');
            const summary = document.getElementById('mobile-summary');
            if (!sheet) return;
            if (window.innerWidth > 1023) return;
            sheet.classList.toggle('expanded');

            if (sheet.classList.contains('expanded')) {
                if (icon) icon.classList.remove('rotate-180');
                if (summary) {
                    summary.classList.add('opacity-0');
                    setTimeout(() => summary.classList.add('hidden'), 300);
                }
            } else {
                if (icon) icon.classList.add('rotate-180');
                if (summary) {
                    summary.classList.remove('hidden');
                    setTimeout(() => summary.classList.remove('opacity-0'), 10);
                }
            }
        }
    </script>
</body>
</html>
