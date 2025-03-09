package com.sivalabs.springblog.domain.services;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownUtils {
    static Parser parser = Parser.builder().build();
    static HtmlRenderer renderer = HtmlRenderer.builder().build();

    public static String toHTML(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
