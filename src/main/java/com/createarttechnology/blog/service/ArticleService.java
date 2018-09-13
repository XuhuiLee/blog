package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.Article;
import com.createarttechnology.blog.dao.mapper.StorageMapper;
import com.createarttechnology.jutil.log.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Service
public class ArticleService {

    private static final Logger logger = Logger.getLogger(ArticleService.class);

    @Resource
    private StorageMapper storageMapper;

    public Article getArticle(long id) {
        try {
            return storageMapper.getArticle(id);
        } catch (Exception e) {
            logger.error("storageMapper.getArticle error, id={}, e:", id, e);
        }
        return null;
    }

    public List<Article> getArticleList() {
        try {
            return storageMapper.getArticleList();
        } catch (Exception e) {
            logger.error("storageMapper.getArticleList error, e:", e);
        }
        return null;
    }

    public long saveArticle(Article article) {
        try {
            storageMapper.saveArticle(article);
            return article.getId();
        } catch (Exception e) {
            logger.error("storageMapper.saveArticle error, article={}, e:", article, e);
        }
        return 0;
    }

    public void updateArticle(Article article) {
        try {
            storageMapper.updateArticle(article.getId(), article);
        } catch (Exception e) {
            logger.error("storageMapper.updateArticle error, article={}, e:", article, e);
        }
    }

}
