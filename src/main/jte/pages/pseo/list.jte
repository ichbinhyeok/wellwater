@param String family
@param java.util.List<com.example.wellwater.pseo.PseoPage> pages

@template.layout.main(title = "Well Water " + family, robots = "index,follow", description = "Browse well water " + family + " pages with practical next steps.", content = @`
    <section class="panel">
        <h1>${family}</h1>
        <p class="muted">High-intent pages for ${family}.</p>
    </section>
    <section class="panel">
        <div class="grid">
            @for (var page : pages)
                <a class="panel" href="/well-water/${page.slug()}">
                    <h2>${page.h1()}</h2>
                    <p class="muted">${page.metaDescription()}</p>
                </a>
            @endfor
        </div>
    </section>
`)
