package com.createarttechnology.blog.util;

import com.createarttechnology.jutil.StringUtil;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class RichTextUtil {


    /**
     * 通用严格过滤方法
     * @param content 需要过滤的富文本
     * @param whiteTagMap key：允许通过的tag名 value：允许通过的attr
     * 一般通用的 whiteTagMap：
    Map<String, List<String>> map = Maps.newHashMap();
    map.put("p", Lists.<String>newArrayList());
    map.put("strong", Lists.<String>newArrayList());
    map.put("img", Lists.newArrayList("src"));
    map.put("video", Lists.newArrayList("src", "poster", "data-duration"));
    map.put("a", Lists.newArrayList("href"));
     * @return 过滤后的富文本
     */
    public static String filterDocumentByJSoup(String content, Map<String, List<String>> whiteTagMap) {
        if (StringUtil.isEmpty(content) || whiteTagMap == null || whiteTagMap.size() == 0) {
            return content;
        }
        Document document = Jsoup.parse(content);
        Elements elements = document.getAllElements();
        if (elements != null && elements.size() > 0) {
            for (Element element : elements) {
                if (element != null) {
                    if (whiteTagMap.containsKey(element.tagName())) {
                        // 白名单 过滤attr
                        List<String> allowAttrs = whiteTagMap.get(element.tagName());
                        if (element.attributes() != null && element.attributes().size() > 0) {
                            for (Attribute attr : element.attributes()) {
                                String key = attr.getKey();
                                String value = attr.getValue();
                                if (!allowAttrs.contains(key) || value.contains("<") || value.contains(">") || value.contains("'") || value.contains("\"")) {
                                    element.removeAttr(key);
                                }
                            }
                        }
                    } else if (!"#root".equals(element.tagName()) && !"body".equals(element.tagName()) && !"html".equals(element.tagName())){
                        // 其他 过滤tag
                        element.remove();
                    }
                }
            }
        }
        return document.body().html()
                .replace("javascript", "");
    }

    public static List<String> extractPicsFromContentByJSoup(String content) {
        if (StringUtil.isEmpty(content)) {
            return null;
        }
        Document document = Jsoup.parse(content);
        Elements elements = document.getAllElements();

        List<String> pics = Lists.newArrayList();
        if (elements != null && elements.size() > 0) {
            for (Element element : elements) {
                if (element != null) {
                    if ("img".equals(element.tagName())) {
                        String src = element.attr("src");
                        if (StringUtil.isNotEmpty(src)) {
                            pics.add(src);
                        }
                    }
                }
            }
        }
        return pics.size() > 0 ? pics : null;
    }

    public static String toSimpleText(String content) {
        if (StringUtil.isEmpty(content)) {
            return "";
        }
        content = content.replaceAll("<br.*>", "\n");
        content = content.replaceAll("<[^>]*>", "");
        content = content.replaceAll("\n+", "\n");
        content = content.replaceAll("\n", "<br/>");
        return content;
    }



}
