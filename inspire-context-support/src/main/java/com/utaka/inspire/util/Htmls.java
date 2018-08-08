/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.io.BaseEncoding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 * @author XINEN
 */
public final class Htmls {

    public static HtmlQuery of(String html) {
        Element body = Jsoup.parse(html).body();
        return new HtmlQuery(body);

    }

    public static boolean containImages(String html) {
        if (StringUtils.isEmpty(html)) {
            return false;
        }

        HtmlQuery query = of(html);
        return (query.selectByTag("img").size() > 0)
                || (query.select("input[type=image]").size() > 0);

    }

    public static String getBodyText(String html) {
        try {
            return Jsoup.parse(html).body().text();

        } catch (Exception e) {
            return Jsoup.parseBodyFragment(html).text();
        }

    }

    public static String clean(String html) {
        return clean(html, false);

    }

    public static String clean(String html, boolean withImages) {
        if (StringUtils.isEmpty(html))
            return StringUtils.EMPTY;

        HtmlQuery query = Htmls.of(html).body().removeStyles().removeScripts();
        if (!withImages) {
            query.suspendImage();
        }
        return query.toString();

        // return Jsoup.clean(Jsoup.parse(html).getElementsByTag("body").first().toString(),
        // withImages ? relaxedWithImages() : relaxed());
    }

    public static String replayImageSourceAsDataBase64Url(String cid,
                                                          String html,
                                                          String mimeType,
                                                          String base64ImageSource) {

        if (html.contains(cid)) {
            String dataUrl = new StringBuilder("data:").append(mimeType).append(";base64,")
                    .append(base64ImageSource).toString();
            html = html.replace("cid:" + cid, dataUrl);
        }
        return html;

    }

    public static String buildImageDataSource(byte[] data, String mimeType) {
        return new StringBuilder("data:")
                .append(mimeType)
                .append(";base64,")
                .append(BaseEncoding.base64().encode(data))
                .toString();
    }

    public static Whitelist relaxed() {
        return Whitelist.none().addTags(
                "a", "b", "blockquote", "br", "caption", "cite", "code", "col",
                "colgroup", "dd", "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6",
                "i", "li", "ol", "p", "pre", "q", "small", "strike", "strong",
                "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u",
                "ul")

                .addAttributes("a", "href", "title", "style")
                .addAttributes("blockquote", "cite", "style")
                .addAttributes("col", "span", "width", "style")
                .addAttributes("colgroup", "span", "width", "style")
                .addAttributes("ol", "start", "type", "style")
                .addAttributes("q", "cite", "style")
                .addAttributes("table", "summary", "width", "style")
                .addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width", "style")
                .addAttributes(
                        "th", "abbr", "axis", "colspan", "rowspan", "scope", "width", "style")
                .addAttributes("ul", "type")

                .addProtocols("a", "href", "ftp", "http", "https", "mailto")
                .addProtocols("blockquote", "cite", "http", "https")
                .addProtocols("q", "cite", "http", "https");

    }

    public static Whitelist relaxedWithImages() {
        return relaxed().addTags("img")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width", "style")
                .addProtocols("img", "src", "http", "https", "cid", "data");

    }

    public static String blockquote(String bodyHtml) {
        Element body = Jsoup.parse(bodyHtml);
        Element replay = new Element(Tag.valueOf("div"), body.baseUri());
        replay.appendElement("div").html("<p> &nbsp; </p><p> &nbsp; </p>");

        replay.appendElement("hr")
                .attr("style",
                        "margin: 18px 0 18px 20px;border: 0;border-bottom: 1px solid #eeeeee;");

        Element original =
                replay.appendElement("div")
                        .appendElement("blockquote")
                        .attr("style",
                                "padding: 0 0 0 15px; margin: 0 0 20px 20px; border-left: 1px solid #eeeeee;");

        original.appendElement("div").html(bodyHtml);

        return replay.toString();

    }

    public static class HtmlQuery {
        private final Element element;

        private HtmlQuery(Element e) {
            this.element = e;

        }

        public HtmlQuery body() {
            return new HtmlQuery(this.element.getElementsByTag("body").first());
        }

        public Elements select(String selector) {
            return this.element.select(selector);

        }

        public Elements selectByTag(String tagName) {
            return this.element.getElementsByTag(tagName);

        }

        public HtmlQuery remove(String selector) {
            Elements es = this.element.select(selector);
            es.remove();
            return this;
        }

        public HtmlQuery removeStyles() {
            return this.removeTag("style");
        }

        public HtmlQuery removeScripts() {
            return this.removeTag("script");
        }

        public HtmlQuery removeTag(String tagName) {
            Elements es = this.element.getElementsByTag(tagName);
            es.remove();
            return this;
        }

        public HtmlQuery removeTagByAttribute(String key) {
            Elements es = this.element.getElementsByAttribute(key);
            es.remove();
            return this;
        }

        public HtmlQuery suspend(String selector) {
            Elements es = this.element.select(selector);
            es.attr("src", "");
            return this;
        }

        public HtmlQuery suspendTag(String tagName) {
            Elements es = this.element.getElementsByTag(tagName);
            es.attr("src", "");
            return this;

        }

        public HtmlQuery suspendImage() {
            return this.suspendTag("img").suspend("input[type=image]");

        }

        @Override
        public String toString() {
            return this.element.toString();

        }
    }
}
