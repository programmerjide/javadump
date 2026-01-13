package io.github.programmerjide.javadump.formatter;

import io.github.programmerjide.javadump.config.DumperConfig;
import io.github.programmerjide.javadump.model.DumpNode;

/**
 * Enhanced HTML formatter with interactive features.
 *
 * @author Olaldejo Olajide
 * @since 1.5.0
 */
public class InteractiveHtmlFormatter extends HtmlFormatter {

    public InteractiveHtmlFormatter(DumperConfig config) {
        super(config);
    }

    @Override
    public String format(DumpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(getInteractiveHeader());
        sb.append("<div class=\"dump-container\">\n");
        sb.append("<div class=\"controls\">\n");
        sb.append(getControls());
        sb.append("</div>\n");
        sb.append("<div class=\"dump-output\" id=\"dumpOutput\">\n");
        sb.append(super.formatFragment(node));
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append(getInteractiveFooter());
        return sb.toString();
    }

    private String getInteractiveHeader() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>JavaDump Interactive</title>
                <style>
            """ + getInteractiveCSS() + """
                </style>
            </head>
            <body>
            """;
    }

    private String getControls() {
        return """
            <div class="toolbar">
                <h2>JavaDump Interactive</h2>
                <div class="button-group">
                    <button onclick="toggleCollapse()">Toggle Collapse</button>
                    <button onclick="copyToClipboard()">Copy</button>
                    <button onclick="downloadDump()">Download</button>
                    <input type="search" id="searchBox" placeholder="Search..." 
                           oninput="searchDump(this.value)">
                    <select id="themeSelector" onchange="changeTheme(this.value)">
                        <option value="dark">Dark</option>
                        <option value="light">Light</option>
                        <option value="dracula">Dracula</option>
                        <option value="monokai">Monokai</option>
                    </select>
                </div>
            </div>
            """;
    }

    private String getInteractiveFooter() {
        return """
            <script>
            """ + getInteractiveJavaScript() + """
            </script>
            </body>
            </html>
            """;
    }

    private String getInteractiveCSS() {
        return """
            * {
                box-sizing: border-box;
            }
            body {
                margin: 0;
                font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
                background: #1e1e1e;
                color: #d4d4d4;
            }
            .dump-container {
                max-width: 1400px;
                margin: 0 auto;
                padding: 20px;
            }
            .toolbar {
                background: #252526;
                border: 1px solid #3c3c3c;
                border-radius: 8px 8px 0 0;
                padding: 15px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .toolbar h2 {
                margin: 0;
                color: #569cd6;
                font-size: 18px;
            }
            .button-group {
                display: flex;
                gap: 10px;
                align-items: center;
            }
            button {
                background: #0e639c;
                color: white;
                border: none;
                padding: 8px 16px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
                transition: background 0.2s;
            }
            button:hover {
                background: #1177bb;
            }
            #searchBox {
                padding: 8px 12px;
                border: 1px solid #3c3c3c;
                border-radius: 4px;
                background: #1e1e1e;
                color: #d4d4d4;
                font-family: inherit;
                width: 200px;
            }
            #themeSelector {
                padding: 8px 12px;
                border: 1px solid #3c3c3c;
                border-radius: 4px;
                background: #1e1e1e;
                color: #d4d4d4;
                font-family: inherit;
                cursor: pointer;
            }
            .dump-output {
                background: #252526;
                border: 1px solid #3c3c3c;
                border-top: none;
                border-radius: 0 0 8px 8px;
                padding: 20px;
                white-space: pre-wrap;
                word-wrap: break-word;
                min-height: 200px;
                max-height: 80vh;
                overflow: auto;
            }
            .collapsible {
                cursor: pointer;
                user-select: none;
            }
            .collapsible:before {
                content: '▼ ';
                display: inline-block;
                margin-right: 5px;
                transition: transform 0.2s;
            }
            .collapsible.collapsed:before {
                transform: rotate(-90deg);
            }
            .collapsed-content {
                display: none;
            }
            .highlight {
                background: rgba(255, 255, 0, 0.3);
            }
            
            /* Theme: Light */
            body.theme-light {
                background: #ffffff;
                color: #000000;
            }
            body.theme-light .toolbar,
            body.theme-light .dump-output {
                background: #f5f5f5;
                border-color: #cccccc;
            }
            body.theme-light #searchBox,
            body.theme-light #themeSelector {
                background: #ffffff;
                color: #000000;
                border-color: #cccccc;
            }
            
            /* Theme: Dracula */
            body.theme-dracula {
                background: #282a36;
                color: #f8f8f2;
            }
            body.theme-dracula .toolbar,
            body.theme-dracula .dump-output {
                background: #21222c;
                border-color: #44475a;
            }
            
            /* Theme: Monokai */
            body.theme-monokai {
                background: #272822;
                color: #f8f8f2;
            }
            body.theme-monokai .toolbar,
            body.theme-monokai .dump-output {
                background: #1e1f1c;
                border-color: #3e3d32;
            }
            """;
    }

    private String getInteractiveJavaScript() {
        return """
            // Make objects collapsible
            function makeCollapsible() {
                const output = document.getElementById('dumpOutput');
                const lines = output.innerHTML.split('\\n');
                let depth = 0;
                let newHtml = '';
                
                lines.forEach((line, i) => {
                    if (line.includes('{')) {
                        depth++;
                        newHtml += '<span class="collapsible" onclick="toggleLine(this)">' + 
                                   line + '</span>\\n';
                    } else if (line.includes('}')) {
                        depth--;
                        newHtml += line + '\\n';
                    } else {
                        newHtml += '<span class="collapsible-line">' + line + '</span>\\n';
                    }
                });
                
                output.innerHTML = newHtml;
            }
            
            function toggleLine(element) {
                element.classList.toggle('collapsed');
                let next = element.nextElementSibling;
                while (next && !next.textContent.includes('}')) {
                    next.classList.toggle('collapsed-content');
                    next = next.nextElementSibling;
                }
            }
            
            function toggleCollapse() {
                document.querySelectorAll('.collapsible').forEach(el => {
                    el.classList.toggle('collapsed');
                });
                document.querySelectorAll('.collapsible-line').forEach(el => {
                    el.classList.toggle('collapsed-content');
                });
            }
            
            function copyToClipboard() {
                const output = document.getElementById('dumpOutput');
                const text = output.innerText;
                
                navigator.clipboard.writeText(text).then(() => {
                    const btn = event.target;
                    const originalText = btn.textContent;
                    btn.textContent = 'Copied!';
                    btn.style.background = '#107c10';
                    setTimeout(() => {
                        btn.textContent = originalText;
                        btn.style.background = '';
                    }, 2000);
                }).catch(err => {
                    alert('Failed to copy: ' + err);
                });
            }
            
            function downloadDump() {
                const output = document.getElementById('dumpOutput');
                const text = output.innerText;
                const blob = new Blob([text], { type: 'text/plain' });
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'dump-' + new Date().getTime() + '.txt';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
            }
            
            function searchDump(query) {
                const output = document.getElementById('dumpOutput');
                const text = output.innerHTML;
                
                // Remove previous highlights
                const cleaned = text.replace(/<mark class="highlight">|<\\/mark>/g, '');
                
                if (!query) {
                    output.innerHTML = cleaned;
                    return;
                }
                
                // Add highlights
                const regex = new RegExp(escapeRegex(query), 'gi');
                const highlighted = cleaned.replace(regex, 
                    match => '<mark class="highlight">' + match + '</mark>');
                
                output.innerHTML = highlighted;
            }
            
            function escapeRegex(str) {
                return str.replace(/[.*+?^${}()|[\\]\\\\]/g, '\\\\$&');
            }
            
            function changeTheme(theme) {
                document.body.className = 'theme-' + theme;
            }
            
            // Initialize
            window.addEventListener('load', () => {
                makeCollapsible();
            });
            """;
    }
}