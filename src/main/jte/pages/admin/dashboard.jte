@param com.example.wellwater.admin.AdminDashboardService.DashboardSnapshot dashboard

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="robots" content="noindex,nofollow,noarchive">
    <title>Admin Dashboard</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&family=Sora:wght@500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg: #0d1410;
            --bg-soft: #121c16;
            --panel: rgba(24, 34, 28, 0.92);
            --panel-strong: #18221c;
            --border: rgba(186, 204, 192, 0.12);
            --text: #eef4ef;
            --muted: #9fb1a5;
            --accent: #58b79c;
            --warm: #dd9350;
            --danger: #d97b6c;
            --shadow: 0 24px 60px rgba(0, 0, 0, 0.26);
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            color: var(--text);
            font-family: "Manrope", sans-serif;
            background:
                radial-gradient(circle at top left, rgba(88, 183, 156, 0.14), transparent 28%),
                radial-gradient(circle at top right, rgba(221, 147, 80, 0.12), transparent 24%),
                linear-gradient(180deg, #0d1410 0%, #111a15 48%, #101712 100%);
        }

        .shell {
            width: min(1240px, calc(100vw - 32px));
            margin: 0 auto;
            padding: 24px 0 56px;
        }

        .hero,
        .panel {
            border: 1px solid var(--border);
            border-radius: 28px;
            background: linear-gradient(180deg, rgba(24, 34, 28, 0.96), rgba(18, 26, 21, 0.92));
            box-shadow: var(--shadow);
        }

        .hero {
            padding: 28px;
            display: grid;
            grid-template-columns: minmax(0, 1.4fr) minmax(280px, 0.9fr);
            gap: 20px;
            margin-bottom: 22px;
        }

        .kicker {
            display: inline-flex;
            align-items: center;
            gap: 10px;
            padding: 7px 12px;
            border-radius: 999px;
            background: rgba(88, 183, 156, 0.12);
            color: var(--accent);
            font-size: 0.74rem;
            font-weight: 800;
            letter-spacing: 0.14em;
            text-transform: uppercase;
        }

        .kicker::before {
            content: "";
            width: 8px;
            height: 8px;
            border-radius: 999px;
            background: var(--accent);
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
            margin-top: 16px;
            font-size: clamp(2.2rem, 4vw, 3.3rem);
        }

        h2 {
            font-size: 1.35rem;
        }

        p {
            margin: 0;
        }

        .hero-copy {
            display: grid;
            gap: 14px;
        }

        .hero-copy p {
            color: var(--muted);
            max-width: 68ch;
        }

        .hero-note {
            display: grid;
            gap: 12px;
            align-content: start;
            padding: 18px;
            border-radius: 22px;
            background: rgba(255, 255, 255, 0.03);
            border: 1px solid rgba(255, 255, 255, 0.06);
        }

        .hero-note strong {
            font-size: 0.98rem;
        }

        .grid {
            display: grid;
            gap: 16px;
        }

        .grid.metrics {
            grid-template-columns: repeat(auto-fit, minmax(170px, 1fr));
            margin-bottom: 16px;
        }

        .grid.double {
            grid-template-columns: repeat(2, minmax(0, 1fr));
        }

        .panel {
            padding: 22px;
        }

        .metric-card {
            padding: 18px;
            border-radius: 20px;
            background: rgba(255, 255, 255, 0.03);
            border: 1px solid rgba(255, 255, 255, 0.06);
        }

        .metric-label {
            color: var(--muted);
            font-size: 0.78rem;
            font-weight: 800;
            letter-spacing: 0.12em;
            text-transform: uppercase;
        }

        .metric-value {
            display: block;
            margin-top: 8px;
            font-size: 1.95rem;
            font-weight: 800;
            letter-spacing: -0.05em;
        }

        .metric-sub {
            margin-top: 6px;
            color: var(--muted);
            font-size: 0.92rem;
        }

        .section-head {
            display: flex;
            align-items: end;
            justify-content: space-between;
            gap: 16px;
            margin-bottom: 18px;
        }

        .section-head p {
            color: var(--muted);
            max-width: 56ch;
        }

        .list {
            display: grid;
            gap: 12px;
        }

        .row {
            display: flex;
            justify-content: space-between;
            gap: 16px;
            padding-bottom: 12px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.06);
        }

        .row:last-child {
            padding-bottom: 0;
            border-bottom: 0;
        }

        .row-label {
            color: var(--muted);
            font-size: 0.95rem;
        }

        .row-value {
            font-weight: 700;
            text-align: right;
        }

        .callout {
            padding: 18px;
            border-radius: 22px;
            background: linear-gradient(180deg, rgba(88, 183, 156, 0.08), rgba(221, 147, 80, 0.06));
            border: 1px solid rgba(88, 183, 156, 0.18);
        }

        .callout ul {
            margin: 12px 0 0;
            padding-left: 18px;
            color: var(--muted);
        }

        .callout li + li {
            margin-top: 8px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 0.92rem;
        }

        th,
        td {
            padding: 10px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.06);
            text-align: left;
            vertical-align: top;
        }

        th {
            color: var(--muted);
            font-size: 0.76rem;
            font-weight: 800;
            letter-spacing: 0.12em;
            text-transform: uppercase;
        }

        td {
            color: var(--text);
        }

        .muted {
            color: var(--muted);
        }

        .badge {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 7px 12px;
            border-radius: 999px;
            background: rgba(221, 147, 80, 0.12);
            color: var(--warm);
            font-size: 0.76rem;
            font-weight: 800;
            letter-spacing: 0.1em;
            text-transform: uppercase;
        }

        @media (max-width: 960px) {
            .hero,
            .grid.double {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<main class="shell">
    <section class="hero">
        <div class="hero-copy">
            <span class="kicker">Internal Only</span>
            <div>
                <h1>Operations Dashboard</h1>
                <p>This page is intentionally unlinked from public navigation and excluded from indexing. It reads only local CSV-backed product data.</p>
            </div>
            <div class="badge">Auth + noindex + no sitemap exposure</div>
        </div>
        <aside class="hero-note">
            <strong>What this page can answer today</strong>
            <p class="muted">Indexable page count, content freshness, internal funnel activity, CTA click patterns, and the strongest slugs currently visible in your own event log.</p>
            <strong>What this page cannot answer yet</strong>
            <p class="muted">Google Search Console impressions, clicks, average position, keyword difficulty, and SERP share of voice are not connected yet.</p>
        </aside>
    </section>

    <section class="grid metrics">
        <article class="metric-card">
            <span class="metric-label">Indexable Pages</span>
            <strong class="metric-value">${dashboard.totalIndexablePages()}</strong>
            <p class="metric-sub">Current public pSEO surface</p>
        </article>
        <article class="metric-card">
            <span class="metric-label">Entry Selections</span>
            <strong class="metric-value">${dashboard.funnelCounts().get("entry_mode_selected")}</strong>
            <p class="metric-sub">Top-of-funnel internal starts</p>
        </article>
        <article class="metric-card">
            <span class="metric-label">Result Views</span>
            <strong class="metric-value">${dashboard.funnelCounts().get("result_viewed")}</strong>
            <p class="metric-sub">Decision results rendered</p>
        </article>
        <article class="metric-card">
            <span class="metric-label">CTA Clicks</span>
            <strong class="metric-value">${dashboard.funnelCounts().get("cta_clicked")}</strong>
            <p class="metric-sub">Commerce or guidance clicks</p>
        </article>
        <article class="metric-card">
            <span class="metric-label">Lead Submissions</span>
            <strong class="metric-value">${dashboard.totalLeads()}</strong>
            <p class="metric-sub">Captured from public research pages</p>
        </article>
        <article class="metric-card">
            <span class="metric-label">Completion Rate</span>
            <strong class="metric-value">${dashboard.completionRate()}</strong>
            <p class="metric-sub">`test_completed / entry_mode_selected`</p>
        </article>
        <article class="metric-card">
            <span class="metric-label">CTA Rate</span>
            <strong class="metric-value">${dashboard.ctaClickRate()}</strong>
            <p class="metric-sub">`cta_clicked / result_viewed`</p>
        </article>
        <article class="metric-card">
            <span class="metric-label">Leads / Page</span>
            <strong class="metric-value">${dashboard.leadsPerPage()}</strong>
            <p class="metric-sub">Simple density check across indexable pages</p>
        </article>
    </section>

    <section class="grid double">
        <article class="panel">
            <div class="section-head">
                <div>
                    <h2>Content Surface</h2>
                    <p>Use this to see whether the indexable footprint is growing and whether the source-backed content is getting refreshed often enough.</p>
                </div>
            </div>
            <div class="list">
                @for (var familyCount : dashboard.familyCounts().entrySet())
                    <div class="row">
                        <span class="row-label">${familyCount.getKey()}</span>
                        <span class="row-value">${familyCount.getValue()}</span>
                    </div>
                @endfor
                <div class="row">
                    <span class="row-label">Newest fetched_at</span>
                    <span class="row-value">${dashboard.newestFetchedAt()}</span>
                </div>
                <div class="row">
                    <span class="row-label">Oldest fetched_at</span>
                    <span class="row-value">${dashboard.oldestFetchedAt()}</span>
                </div>
                <div class="row">
                    <span class="row-label">Newest search_performed_at</span>
                    <span class="row-value">${dashboard.newestSearchAt()}</span>
                </div>
                <div class="row">
                    <span class="row-label">Oldest search_performed_at</span>
                    <span class="row-value">${dashboard.oldestSearchAt()}</span>
                </div>
            </div>
        </article>

        <article class="panel">
            <div class="section-head">
                <div>
                    <h2>Internal Funnel</h2>
                    <p>These are product-side events only. They are useful for product QA, but not a substitute for search visibility data.</p>
                </div>
            </div>
            <div class="list">
                @for (var funnel : dashboard.funnelCounts().entrySet())
                    <div class="row">
                        <span class="row-label">${funnel.getKey()}</span>
                        <span class="row-value">${funnel.getValue()}</span>
                    </div>
                @endfor
            </div>
        </article>

        <article class="panel">
            <div class="section-head">
                <div>
                    <h2>Top Entry Modes & Branches</h2>
                    <p>Quick read on how users are entering the tool and what branch mix your current event stream is producing.</p>
                </div>
            </div>
            <div class="grid double">
                <div class="list">
                    <h3>Entry Modes</h3>
                    @if(dashboard.entryModes().isEmpty())
                        <p class="muted">No analytics rows yet.</p>
                    @else
                        @for (var row : dashboard.entryModes())
                            <div class="row">
                                <span class="row-label">${row.label()}</span>
                                <span class="row-value">${row.count()}</span>
                            </div>
                        @endfor
                    @endif
                </div>
                <div class="list">
                    <h3>Branches</h3>
                    @if(dashboard.topBranches().isEmpty())
                        <p class="muted">No branch rows yet.</p>
                    @else
                        @for (var row : dashboard.topBranches())
                            <div class="row">
                                <span class="row-label">${row.label()}</span>
                                <span class="row-value">${row.count()}</span>
                            </div>
                        @endfor
                    @endif
                </div>
            </div>
        </article>

        <article class="panel">
            <div class="section-head">
                <div>
                    <h2>Top Slugs & CTA Types</h2>
                    <p>Once landing pages start producing volume, this is the first place to see whether certain families or CTA patterns dominate.</p>
                </div>
            </div>
            <div class="grid double">
                <div class="list">
                    <h3>Top Slugs</h3>
                    @if(dashboard.topSlugs().isEmpty())
                        <p class="muted">No slug rows yet.</p>
                    @else
                        @for (var row : dashboard.topSlugs())
                            <div class="row">
                                <span class="row-label">${row.label()}</span>
                                <span class="row-value">${row.count()}</span>
                            </div>
                        @endfor
                    @endif
                </div>
                <div class="list">
                    <h3>CTA Types</h3>
                    @if(dashboard.topCtaTypes().isEmpty())
                        <p class="muted">No CTA click rows yet.</p>
                    @else
                        @for (var row : dashboard.topCtaTypes())
                            <div class="row">
                                <span class="row-label">${row.label()}</span>
                                <span class="row-value">${row.count()}</span>
                            </div>
                        @endfor
                    @endif
                </div>
            </div>
        </article>

        <article class="panel">
            <div class="section-head">
                <div>
                    <h2>Lead Families & Lead Slugs</h2>
                    <p>This is the first real revenue-side check: which page families and slugs are producing direct follow-up intent.</p>
                </div>
            </div>
            <div class="grid double">
                <div class="list">
                    <h3>Lead Families</h3>
                    @if(dashboard.leadFamilies().isEmpty())
                        <p class="muted">No lead rows yet.</p>
                    @else
                        @for (var row : dashboard.leadFamilies())
                            <div class="row">
                                <span class="row-label">${row.label()}</span>
                                <span class="row-value">${row.count()}</span>
                            </div>
                        @endfor
                    @endif
                </div>
                <div class="list">
                    <h3>Lead Slugs</h3>
                    @if(dashboard.leadSlugs().isEmpty())
                        <p class="muted">No lead rows yet.</p>
                    @else
                        @for (var row : dashboard.leadSlugs())
                            <div class="row">
                                <span class="row-label">${row.label()}</span>
                                <span class="row-value">${row.count()}</span>
                            </div>
                        @endfor
                    @endif
                </div>
            </div>
        </article>
    </section>

    <section class="panel" style="margin-top: 16px;">
        <div class="section-head">
            <div>
                <h2>Search Visibility Gaps</h2>
                <p>For a KRW 1M/month goal, the business bottleneck is not internal CTA polish yet. It is search visibility measurement and landing-page attribution.</p>
            </div>
        </div>
        <div class="callout">
            <strong>Still missing for real SEO operations</strong>
            <ul>
                <li>Google Search Console data: impressions, clicks, CTR, average position, and queries per landing page</li>
                <li>Keyword cluster sheet: target query, intent, SERP type, difficulty proxy, and owner per page family</li>
                <li>Rank tracking for money pages and highest-intent symptom/contaminant pages</li>
                <li>Revenue attribution from landing slug -> result view -> CTA click -> lead or affiliate outcome</li>
            </ul>
        </div>
    </section>

    <section class="panel" style="margin-top: 16px;">
        <div class="section-head">
            <div>
                <h2>Recent Leads</h2>
                <p>Most recent submissions from the public lead capture forms. This is the queue the site is now producing from organic and research traffic.</p>
            </div>
        </div>
        @if(dashboard.recentLeads().isEmpty())
            <p class="muted">No leads have been captured yet.</p>
        @else
            <table style="margin-bottom: 24px;">
                <thead>
                <tr>
                    <th>When</th>
                    <th>Email</th>
                    <th>State</th>
                    <th>Family</th>
                    <th>Slug</th>
                    <th>Note</th>
                </tr>
                </thead>
                <tbody>
                @for (var lead : dashboard.recentLeads())
                    <tr>
                        <td>${lead.submittedAt()}</td>
                        <td>${lead.email()}</td>
                        <td>${lead.state()}</td>
                        <td>${lead.sourceFamily()}</td>
                        <td>${lead.sourceSlug()}</td>
                        <td class="muted">${lead.note()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </section>

    <section class="panel" style="margin-top: 16px;">
        <div class="section-head">
            <div>
                <h2>Recent Events</h2>
                <p>Most recent internal analytics rows from the configured analytics CSV.</p>
            </div>
        </div>
        @if(dashboard.recentEvents().isEmpty())
            <p class="muted">No analytics events have been recorded yet.</p>
        @else
            <table>
                <thead>
                <tr>
                    <th>When</th>
                    <th>Event</th>
                    <th>Entry</th>
                    <th>Slug</th>
                    <th>Branch</th>
                    <th>CTA</th>
                    <th>Note</th>
                </tr>
                </thead>
                <tbody>
                @for (var event : dashboard.recentEvents())
                    <tr>
                        <td>${event.happenedAt()}</td>
                        <td>${event.eventName()}</td>
                        <td>${event.entryMode()}</td>
                        <td>${event.slug()}</td>
                        <td>${event.branch()}</td>
                        <td>${event.ctaType()}</td>
                        <td class="muted">${event.note()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </section>
</main>
</body>
</html>
