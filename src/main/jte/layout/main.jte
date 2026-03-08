@param String title
@param String robots
@param String description
@param String canonicalUrl
@param java.util.List<com.example.wellwater.web.page.SeoBreadcrumb> breadcrumbs
@param java.util.List<String> structuredDataBlocks
@param String openGraphType
@param String socialImageUrl
@param String googleSiteVerification
@param String googleAnalyticsId
@param gg.jte.Content content

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="robots" content="${robots}">
    <meta name="description" content="${description}">
    <meta property="og:site_name" content="Water Verdict">
    <meta property="og:type" content="${openGraphType}">
    <meta property="og:title" content="${title}">
    <meta property="og:description" content="${description}">
    @if (canonicalUrl != null && !canonicalUrl.isBlank())
        <meta property="og:url" content="${canonicalUrl}">
    @endif
    @if (socialImageUrl != null && !socialImageUrl.isBlank())
        <meta property="og:image" content="${socialImageUrl}">
        <meta name="twitter:image" content="${socialImageUrl}">
    @endif
    <meta name="twitter:card" content="${socialImageUrl != null && !socialImageUrl.isBlank() ? "summary_large_image" : "summary"}">
    <meta name="twitter:title" content="${title}">
    <meta name="twitter:description" content="${description}">
    @if (canonicalUrl != null && !canonicalUrl.isBlank())
        <meta name="twitter:url" content="${canonicalUrl}">
    @endif
    @if (googleSiteVerification != null && !googleSiteVerification.isBlank())
        <meta name="google-site-verification" content="${googleSiteVerification}">
    @endif
    @if (canonicalUrl != null && !canonicalUrl.isBlank())
        <link rel="canonical" href="${canonicalUrl}">
    @endif
    <title>${title}</title>
    <link rel="icon" type="image/svg+xml" href="/favicon.svg">
    @if (googleAnalyticsId != null && !googleAnalyticsId.isBlank())
        <script async src="https://www.googletagmanager.com/gtag/js?id=${googleAnalyticsId}"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag() { dataLayer.push(arguments); }
            gtag('js', new Date());
            gtag('config', '${googleAnalyticsId}', { 'anonymize_ip': true });
        </script>
    @endif
    <script defer src="/analytics.js"></script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Sora:wght@500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg: #f5f6f2;
            --bg-strong: #e6eadb;
            --surface: rgba(255, 255, 255, 0.78);
            --surface-strong: #ffffff;
            --surface-tint: rgba(231, 238, 224, 0.72);
            --text: #142218;
            --muted: #4a594d;
            --muted-strong: #354337;
            --border: rgba(20, 34, 24, 0.12);
            --border-strong: rgba(20, 34, 24, 0.18);
            --accent: #0f766e;
            --accent-soft: rgba(15, 118, 110, 0.12);
            --accent-strong: #0f4b57;
            --warm: #c66a2b;
            --hero-muted: rgba(246, 251, 249, 0.9);
            --hero-chip-bg: rgba(255, 255, 255, 0.14);
            --hero-chip-border: rgba(255, 255, 255, 0.2);
            --hero-chip-text: #f4fbf9;
            --hero-chip-dot: #9ce5d4;
            --shadow-soft: 0 18px 46px rgba(35, 52, 39, 0.08);
            --shadow-card: 0 20px 60px rgba(35, 52, 39, 0.12);
            --radius-xl: 30px;
            --radius-lg: 22px;
            --radius-md: 16px;
            --page-width: min(1180px, calc(100vw - 40px));
        }

        * {
            box-sizing: border-box;
        }

        html {
            scroll-behavior: smooth;
        }

        body {
            margin: 0;
            color: var(--text);
            background:
                radial-gradient(circle at top left, rgba(15, 118, 110, 0.14), transparent 32%),
                radial-gradient(circle at top right, rgba(198, 106, 43, 0.12), transparent 30%),
                linear-gradient(180deg, #f3f6ee 0%, #eef1e8 48%, #f8f8f4 100%);
            font-family: "Manrope", sans-serif;
            line-height: 1.6;
            min-height: 100vh;
        }

        body::before,
        body::after {
            content: "";
            position: fixed;
            inset: auto;
            width: 360px;
            height: 360px;
            border-radius: 999px;
            pointer-events: none;
            filter: blur(30px);
            opacity: 0.38;
            z-index: 0;
        }

        body::before {
            top: -120px;
            right: -120px;
            background: rgba(15, 118, 110, 0.16);
        }

        body::after {
            bottom: -160px;
            left: -140px;
            background: rgba(198, 106, 43, 0.14);
        }

        a {
            color: inherit;
            text-decoration: none;
        }

        img {
            max-width: 100%;
            display: block;
        }

        .shell {
            position: relative;
            z-index: 1;
            width: var(--page-width);
            margin: 0 auto;
            padding: 24px 0 64px;
        }

        .site-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 18px;
            margin-bottom: 30px;
            padding: 16px 18px;
            background: rgba(255, 255, 255, 0.54);
            border: 1px solid rgba(255, 255, 255, 0.8);
            backdrop-filter: blur(18px);
            border-radius: 999px;
            box-shadow: 0 12px 34px rgba(25, 41, 28, 0.06);
        }

        .brand {
            display: flex;
            align-items: center;
            gap: 14px;
            min-width: 0;
        }

        .brand-mark {
            width: 48px;
            height: 48px;
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 16px 26px rgba(15, 118, 110, 0.22);
            overflow: hidden;
        }

        .brand-copy {
            min-width: 0;
        }

        .brand-kicker {
            margin: 0 0 2px;
            font-size: 0.72rem;
            letter-spacing: 0.16em;
            text-transform: uppercase;
            color: var(--muted-strong);
        }

        .brand-title {
            margin: 0;
            font-size: 1rem;
            font-weight: 800;
            letter-spacing: -0.03em;
        }

        .site-nav {
            display: flex;
            align-items: center;
            gap: 10px;
            flex-wrap: wrap;
            justify-content: flex-end;
        }

        .nav-link {
            padding: 10px 14px;
            border-radius: 999px;
            color: var(--muted-strong);
            font-size: 0.92rem;
            font-weight: 700;
            transition: background-color 160ms ease, color 160ms ease, transform 160ms ease;
        }

        .nav-link:hover {
            background: rgba(20, 34, 24, 0.06);
            color: var(--text);
            transform: translateY(-1px);
        }

        .breadcrumb-bar {
            display: flex;
            align-items: center;
            gap: 10px;
            flex-wrap: wrap;
            margin: 0 0 22px;
            padding: 12px 16px;
            background: rgba(255, 255, 255, 0.62);
            border: 1px solid rgba(255, 255, 255, 0.85);
            border-radius: 999px;
            box-shadow: 0 10px 28px rgba(25, 41, 28, 0.05);
            color: var(--muted-strong);
            font-size: 0.9rem;
            font-weight: 700;
        }

        .breadcrumb-sep {
            color: rgba(20, 34, 24, 0.42);
        }

        .breadcrumb-current {
            color: var(--text);
        }

        .form-grid {
            display: grid;
            gap: 12px;
        }

        .form-grid.two {
            grid-template-columns: repeat(2, minmax(0, 1fr));
        }

        .form-field,
        .form-textarea {
            width: 100%;
            padding: 13px 14px;
            border-radius: 14px;
            border: 1px solid var(--border-strong);
            background: rgba(255, 255, 255, 0.88);
            color: var(--text);
            font: inherit;
        }

        .form-field:focus,
        .form-textarea:focus {
            outline: none;
            border-color: var(--text);
            box-shadow: 0 0 0 1px rgba(20, 34, 24, 0.12);
        }

        .form-textarea {
            min-height: 120px;
            resize: vertical;
        }

        .nav-link.nav-link-strong {
            background: linear-gradient(135deg, rgba(15, 118, 110, 0.14), rgba(15, 118, 110, 0.24));
            color: var(--accent-strong);
        }

        .stack {
            display: grid;
            gap: 24px;
        }

        .hero {
            position: relative;
            overflow: hidden;
            padding: 34px;
            border-radius: var(--radius-xl);
            background:
                linear-gradient(135deg, rgba(9, 32, 28, 0.92), rgba(15, 64, 74, 0.92) 54%, rgba(198, 106, 43, 0.82) 100%);
            color: #f6fbf9;
            box-shadow: var(--shadow-card);
        }

        .hero::after {
            content: "";
            position: absolute;
            inset: auto -90px -120px auto;
            width: 260px;
            height: 260px;
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.12);
            filter: blur(0);
        }

        .hero-grid {
            position: relative;
            z-index: 1;
            display: grid;
            grid-template-columns: minmax(0, 1.6fr) minmax(280px, 0.9fr);
            gap: 28px;
            align-items: end;
        }

        .eyebrow {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 7px 12px;
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.13);
            color: rgba(246, 251, 249, 0.88);
            font-size: 0.76rem;
            font-weight: 700;
            letter-spacing: 0.14em;
            text-transform: uppercase;
        }

        .eyebrow::before {
            content: "";
            width: 8px;
            height: 8px;
            border-radius: 999px;
            background: #9ce5d4;
            box-shadow: 0 0 0 6px rgba(156, 229, 212, 0.16);
        }

        h1,
        h2,
        h3,
        h4 {
            margin: 0;
            font-family: "Sora", sans-serif;
            letter-spacing: -0.04em;
            line-height: 1.08;
        }

        h1 {
            margin-top: 18px;
            font-size: clamp(2.5rem, 5vw, 4.4rem);
        }

        h2 {
            font-size: clamp(1.6rem, 2.4vw, 2.15rem);
        }

        h3 {
            font-size: 1.28rem;
        }

        p {
            margin: 0;
        }

        .hero-copy {
            max-width: 720px;
        }

        .hero-lead {
            margin-top: 18px;
            font-size: 1.08rem;
            color: var(--hero-muted);
            max-width: 58ch;
        }

        .hero-actions {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 26px;
        }

        .hero-note {
            display: grid;
            gap: 12px;
            padding: 22px;
            border-radius: 24px;
            background: rgba(255, 255, 255, 0.12);
            border: 1px solid rgba(255, 255, 255, 0.16);
            backdrop-filter: blur(12px);
        }

        .hero-note strong {
            font-size: 1rem;
        }

        .hero-metrics {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 12px;
        }

        .metric {
            padding: 14px 16px;
            border-radius: 18px;
            background: rgba(255, 255, 255, 0.1);
        }

        .metric strong {
            display: block;
            font-size: 1.2rem;
            letter-spacing: -0.04em;
        }

        .metric span {
            font-size: 0.84rem;
            color: rgba(246, 251, 249, 0.84);
        }

        .panel {
            position: relative;
            overflow: hidden;
            padding: 26px;
            border-radius: var(--radius-lg);
            border: 1px solid var(--border);
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.82), rgba(255, 255, 255, 0.7));
            backdrop-filter: blur(16px);
            box-shadow: var(--shadow-soft);
        }

        .panel::before {
            content: "";
            position: absolute;
            inset: 0;
            background: linear-gradient(180deg, rgba(255, 255, 255, 0.2), transparent 42%);
            pointer-events: none;
        }

        .panel > * {
            position: relative;
            z-index: 1;
        }

        .panel-accent {
            background:
                linear-gradient(140deg, rgba(15, 118, 110, 0.12), rgba(255, 255, 255, 0.88) 34%, rgba(198, 106, 43, 0.08));
        }

        .section-header {
            display: flex;
            justify-content: space-between;
            align-items: end;
            gap: 18px;
            margin-bottom: 20px;
        }

        .section-copy {
            max-width: 58ch;
            color: var(--muted);
        }

        .grid {
            display: grid;
            gap: 16px;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        }

        .grid-compact {
            grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
        }

        .card-link {
            display: grid;
            gap: 12px;
            min-height: 100%;
            padding: 22px;
            border-radius: var(--radius-md);
            border: 1px solid rgba(20, 34, 24, 0.08);
            background: linear-gradient(180deg, rgba(255, 255, 255, 0.88), rgba(244, 247, 241, 0.92));
            box-shadow: 0 14px 34px rgba(31, 48, 34, 0.08);
            transition: transform 180ms ease, border-color 180ms ease, box-shadow 180ms ease, background-color 180ms ease;
        }

        .card-link:hover {
            transform: translateY(-3px);
            border-color: rgba(15, 118, 110, 0.24);
            box-shadow: 0 24px 42px rgba(31, 48, 34, 0.12);
        }

        .card-link.card-link-accent {
            background: linear-gradient(160deg, rgba(15, 118, 110, 0.1), rgba(255, 255, 255, 0.92) 40%, rgba(230, 157, 100, 0.14));
        }

        .card-kicker {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            color: var(--accent-strong);
            font-size: 0.74rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.12em;
        }

        .card-kicker::before {
            content: "";
            width: 7px;
            height: 7px;
            border-radius: 999px;
            background: var(--warm);
        }

        .card-meta {
            color: var(--muted);
            font-size: 0.94rem;
        }

        .muted {
            color: var(--muted);
        }

        .small {
            font-size: 0.92rem;
        }

        .warn {
            color: #a54b24;
        }

        .chip-row {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .chip {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 9px 14px;
            border-radius: 999px;
            background: rgba(15, 118, 110, 0.08);
            border: 1px solid rgba(15, 118, 110, 0.12);
            color: var(--accent-strong);
            font-size: 0.83rem;
            font-weight: 700;
        }

        .chip::before {
            content: "";
            width: 7px;
            height: 7px;
            border-radius: 999px;
            background: var(--accent);
        }

        .hero .chip {
            background: var(--hero-chip-bg);
            border-color: var(--hero-chip-border);
            color: var(--hero-chip-text);
        }

        .hero .chip::before {
            background: var(--hero-chip-dot);
        }

        .hero .muted {
            color: var(--hero-muted) !important;
        }

        .mobile-only {
            display: none;
        }

        .prose {
            max-width: 72ch;
            color: var(--text);
        }

        .prose p + p {
            margin-top: 14px;
        }

        .cta-row {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            margin-top: 22px;
        }

        .button-link,
        .button-link-secondary {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            padding: 14px 18px;
            border-radius: 999px;
            font-weight: 800;
            letter-spacing: -0.02em;
            transition: transform 160ms ease, box-shadow 160ms ease, background-color 160ms ease;
        }

        .button-link {
            color: #f4fbf9;
            background: linear-gradient(135deg, var(--accent), var(--accent-strong));
            box-shadow: 0 18px 28px rgba(15, 118, 110, 0.24);
        }

        .button-link-secondary {
            color: var(--text);
            background: rgba(255, 255, 255, 0.72);
            border: 1px solid rgba(20, 34, 24, 0.1);
        }

        .button-link:hover,
        .button-link-secondary:hover {
            transform: translateY(-2px);
        }

        .detail-grid {
            display: grid;
            grid-template-columns: minmax(0, 1.2fr) minmax(280px, 0.8fr);
            gap: 20px;
        }

        .detail-stack {
            display: grid;
            gap: 18px;
        }

        .meta-list {
            display: grid;
            gap: 12px;
        }

        .meta-item {
            padding-top: 12px;
            border-top: 1px solid rgba(20, 34, 24, 0.08);
        }

        .meta-label {
            display: block;
            margin-bottom: 6px;
            color: var(--muted);
            font-size: 0.78rem;
            font-weight: 800;
            letter-spacing: 0.14em;
            text-transform: uppercase;
        }

        .meta-value {
            color: var(--text);
            font-size: 0.95rem;
            word-break: break-word;
        }

        .site-footer {
            margin-top: 24px;
            padding: 0 6px;
            color: var(--muted);
            font-size: 0.88rem;
        }

        .site-footer strong {
            color: var(--text);
        }

        .footer-links {
            display: flex;
            flex-wrap: wrap;
            gap: 12px 18px;
            margin-top: 12px;
        }

        @media (max-width: 960px) {
            .hero-grid,
            .detail-grid {
                grid-template-columns: 1fr;
            }

            .form-grid.two {
                grid-template-columns: 1fr;
            }

            .shell {
                width: min(100vw - 28px, 1180px);
                padding-top: 18px;
            }

            .site-header {
                border-radius: 28px;
                align-items: start;
                flex-direction: column;
                gap: 10px;
                margin-bottom: 22px;
                padding: 14px 16px;
            }

            .site-nav {
                width: 100%;
                justify-content: flex-start;
                flex-wrap: nowrap;
                gap: 8px;
                overflow-x: auto;
                padding-bottom: 8px;
                -webkit-overflow-scrolling: touch;
                scrollbar-width: none;
            }

            .site-nav::-webkit-scrollbar {
                display: none;
            }

            .nav-link {
                flex: 0 0 auto;
            }

            .hero {
                padding: 28px;
            }

            .panel {
                padding: 22px;
            }
        }

        @media (max-width: 640px) {
            h1 {
                font-size: 2.3rem;
            }

            .stack {
                gap: 20px;
            }

            .panel,
            .hero {
                border-radius: 24px;
            }

            .card-link {
                padding: 18px;
            }

            .section-header {
                align-items: start;
                flex-direction: column;
            }

            .section-copy {
                max-width: none;
                font-size: 0.94rem;
            }

            .hero-note {
                padding: 18px;
            }

            .metric {
                padding: 12px 14px;
            }

            .site-header {
                gap: 8px;
                margin-bottom: 18px;
                padding: 12px 14px;
            }

            .brand {
                gap: 10px;
            }

            .brand-mark {
                width: 42px;
                height: 42px;
                border-radius: 14px;
            }

            .site-nav {
                margin-top: -2px;
            }

            .nav-link {
                padding: 9px 12px;
                font-size: 0.88rem;
            }

            .mobile-only {
                display: block;
            }

            .desktop-only,
            .mobile-card-hidden {
                display: none !important;
            }
        }
    </style>
    @for (var structuredData : structuredDataBlocks)
        <script type="application/ld+json">$unsafe{structuredData}</script>
    @endfor
</head>
<body>
<div class="shell">
    <header class="site-header">
        <div class="brand">
            <a href="/" class="brand-mark" aria-label="Water Verdict">
                <img src="/favicon.svg" alt="Water Verdict logo" class="w-full h-full object-cover">
            </a>
            <div class="brand-copy">
                <p class="brand-kicker">Private Well Decision Engine</p>
                <p class="brand-title">Water Verdict</p>
            </div>
        </div>
        <nav class="site-nav" aria-label="Primary">
            <a class="nav-link" href="/well-water/family/contaminants">Contaminants</a>
            <a class="nav-link" href="/well-water/family/symptoms">Symptoms</a>
            <a class="nav-link" href="/well-water/family/compares">Comparisons</a>
            <a class="nav-link" href="/well-water/family/triggers">Triggers</a>
            <a class="nav-link" href="/well-water/family/regional">Regional</a>
            <a class="nav-link" href="/well-water/family/authority">Articles</a>
            <a class="nav-link" href="/trust">Trust</a>
            <a class="nav-link nav-link-strong" href="/tool/result-first">Open Tool</a>
        </nav>
    </header>

    @if (breadcrumbs != null && !breadcrumbs.isEmpty())
        <nav class="breadcrumb-bar" aria-label="Breadcrumb">
            @for (int i = 0; i < breadcrumbs.size(); i++)
                @if (i > 0)
                    <span class="breadcrumb-sep">/</span>
                @endif
                @if (i == breadcrumbs.size() - 1)
                    <span class="breadcrumb-current">${breadcrumbs.get(i).label()}</span>
                @else
                    <a href="${breadcrumbs.get(i).absoluteUrl()}">${breadcrumbs.get(i).label()}</a>
                @endif
            @endfor
        </nav>
    @endif

    ${content}

    <footer class="site-footer">
        <strong>Water Verdict</strong> helps you move from a water clue or lab result to the next test, comparison, or follow-up step.
        <nav class="footer-links" aria-label="Trust and policy">
            <a href="/trust">Trust hub</a>
            <a href="/trust/methodology">Methodology</a>
            <a href="/trust/review-policy">Review policy</a>
            <a href="/trust/reviewers-and-expertise">Reviewers</a>
            <a href="/trust/sources-policy">Sources policy</a>
            <a href="/trust/safety-and-scope">Safety and scope</a>
            <a href="/trust/privacy-and-data-handling">Privacy and data</a>
            <a href="/trust/commercial-disclosure">Commercial disclosure</a>
        </nav>
    </footer>
</div>
</body>
</html>
