@param com.example.wellwater.pseo.PseoPage page

@template.layout.main(title = page.title(), robots = "index,follow", description = page.metaDescription(), content = @`
    <section class="panel">
        <p class="small muted">${page.family()}</p>
        <h1>${page.h1()}</h1>
        <p>${page.intro()}</p>
    </section>

    <section class="panel">
        <h2>What To Do Now</h2>
        <p>${page.actionNow()}</p>
    </section>

    <section class="panel">
        <h2>What To Test Or Compare</h2>
        <p>${page.whatToTest()}</p>
    </section>

    <section class="panel">
        <h2>Primary Next Step</h2>
        <p><a href="${page.primaryCtaUrl()}">${page.primaryCtaLabel()}</a></p>
    </section>

    <section class="panel">
        <h2>Options And Pricing Research</h2>
        <p><a href="${page.moneyCtaUrl()}">${page.moneyCtaLabel()}</a></p>
        <p class="small muted">${page.disclosure()}</p>
    </section>

    <section class="panel">
        <h2>Source</h2>
        <p class="small"><a href="${page.sourceUrl()}">${page.sourceUrl()}</a></p>
        <p class="small muted">search query: ${page.searchQuery()}</p>
        <p class="small muted">searched at: ${page.searchPerformedAt()} / fetched at: ${page.fetchedAt()}</p>
    </section>
`)
