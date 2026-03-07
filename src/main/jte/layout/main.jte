@param String title
@param String robots
@param String description
@param gg.jte.Content content

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="robots" content="${robots}">
    <meta name="description" content="${description}">
    <title>${title}</title>
    <style>
        :root { --text: #111; --muted: #444; --border: #ccc; }
        * { box-sizing: border-box; }
        body { margin: 0; font-family: Arial, sans-serif; color: var(--text); background: #fff; }
        .shell {
            max-width: 1080px;
            margin: 0 auto;
            padding: 24px 16px 48px;
        }
        .panel {
            border: 1px solid var(--border);
            border-radius: 8px;
            padding: 16px;
            margin-bottom: 16px;
        }
        h1, h2, h3 { margin-top: 0; }
        a { color: #0a4e93; text-decoration: none; }
        .grid {
            display: grid;
            gap: 12px;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        }
        .muted { color: var(--muted); }
        .warn { color: #9b1c1c; }
        .small { font-size: 0.92rem; }
        .chip {
            display: inline-block;
            border: 1px solid var(--border);
            border-radius: 999px;
            padding: 4px 10px;
            font-size: 0.85rem;
            margin-right: 6px;
        }
        input, select, button {
            width: 100%;
            margin-top: 8px;
            border: 1px solid var(--border);
            border-radius: 4px;
            padding: 10px;
            font: inherit;
            background: #fff;
        }
        button {
            width: auto;
            background: #1f2937;
            color: #fff;
            border: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="shell">
    @content()
</div>
</body>
</html>
