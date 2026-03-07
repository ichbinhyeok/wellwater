@param java.util.Map<String, Long> familyCounts
@param java.util.List<com.example.wellwater.pseo.PseoPage> featuredPages

@template.layout.main(title = "Well Water pSEO Hub", robots = "index,follow", description = "Well water guides for contaminants symptoms compares and trigger events.", content = @`
    <section class="panel">
        <h1>Well Water Guides and Next Steps</h1>
        <p class="muted">MVP focus: high-intent pSEO pages that drive clear next actions.</p>
    </section>

    <section class="panel">
        <h2>Page Families</h2>
        <div class="grid">
            <a class="panel" href="/well-water/family/contaminants">
                <h3>Contaminants</h3>
                <p class="muted">${familyCounts.get("contaminants")} pages</p>
            </a>
            <a class="panel" href="/well-water/family/symptoms">
                <h3>Symptoms</h3>
                <p class="muted">${familyCounts.get("symptoms")} pages</p>
            </a>
            <a class="panel" href="/well-water/family/compares">
                <h3>Compares</h3>
                <p class="muted">${familyCounts.get("compares")} pages</p>
            </a>
            <a class="panel" href="/well-water/family/triggers">
                <h3>Triggers</h3>
                <p class="muted">${familyCounts.get("triggers")} pages</p>
            </a>
        </div>
    </section>

    <section class="panel">
        <h2>Featured Pages</h2>
        <div class="grid">
            @for (var page : featuredPages)
                <a class="panel" href="/well-water/${page.slug()}">
                    <h3>${page.h1()}</h3>
                    <p class="small muted">${page.family()}</p>
                </a>
            @endfor
        </div>
    </section>
`)
