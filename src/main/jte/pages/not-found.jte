@param String path

@template.layout.main(title = "Not Found", robots = "noindex,nofollow", description = "Requested page was not found.", content = @`
    <section class="panel">
        <h1>Page Not Found</h1>
        <p class="muted">No page data found for: ${path}</p>
        <p><a href="/">Back to home</a></p>
    </section>
`)
