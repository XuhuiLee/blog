package com.createarttechnology.blog.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.annotation.Resource;

import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.jutil.CollectionUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

/**
 * 站点地图
 * @author lixuhui
 */
@Service
public class SiteMapService {

    @Resource
    private ReadService readService;

    public String getSiteMapForBaidu() throws IOException {
        List<ArticleEntity> articles = readService.getAllArticleIdAndUpdateTime();
        Document document = DocumentHelper.createDocument();
        Element urlset = document.addElement("urlset");

        return buildItems(urlset, articles);
    }

    public String getSiteMapForGoogle() throws IOException {
        List<ArticleEntity> articles = readService.getAllArticleIdAndUpdateTime();
        Document document = DocumentHelper.createDocument();
        Element urlset = document.addElement("urlset").addAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");

        return buildItems(urlset, articles);
    }

    private static String buildItems(Element urlset, List<ArticleEntity> articles) throws IOException {
        if (CollectionUtil.isNotEmpty(articles)) {
            articles.forEach(article -> buildItem(urlset, article));
        }

        StringWriter stringWriter = new StringWriter();
        XMLWriter writer = new XMLWriter(stringWriter);
        writer.write(urlset.getDocument());
        writer.close();

        return stringWriter.toString();
    }

    private static void buildItem(Element urlset, ArticleEntity article) {
        if (article == null) {
            return;
        }
        Element url = urlset.addElement("url");
        url.addElement("loc").addText("http://www.createarttechnology.com/blog/article/" + article.getId());
        url.addElement("lastmod").addText(DateFormatUtils.format(article.getUpdateTime() * 1000L, "yyyy-MM-dd"));
    }

}
