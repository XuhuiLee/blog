package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.Pager;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.dao.entity.TagEntity;
import com.createarttechnology.blog.dao.mapper.ArticleMapper;
import com.createarttechnology.blog.dao.mapper.TagMapper;
import com.createarttechnology.logger.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 提供mapper的间接调用
 * Created by lixuhui on 2018/9/13.
 */
@Service
public class StorageService {

    private static final Logger logger = Logger.getLogger(StorageService.class);

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TagMapper tagMapper;

    /*
    article
     */

    public ArticleEntity getArticleEntity(long id) {
        try {
            return articleMapper.getArticle(id);
        } catch (Exception e) {
            logger.error("articleMapper.getArticle error, id={}, e:", id, e);
        }
        return null;
    }

    public List<ArticleEntity> getArticleEntityListByTagId(int tagId) {
        try {
            return articleMapper.getArticleListByTagId(tagId);
        } catch (Exception e) {
            logger.error("articleMapper.getArticleListByTagId error, tagId={}, e:", tagId, e);
        }
        return null;
    }

    public List<ArticleEntity> getArticleListByParentTagId(Collection<Integer> tagIds) {
        try {
            return articleMapper.getArticleListByParentTagId(tagIds);
        } catch (Exception e) {
            logger.error("articleMapper.getArticleListByParentTagId error, tagIds={}, e:", tagIds, e);
        }
        return null;
    }

    public long saveArticleEntity(ArticleEntity article) {
        try {
            articleMapper.saveArticle(article);
            return article.getId();
        } catch (Exception e) {
            logger.error("articleMapper.saveArticle error, article={}, e:", article, e);
        }
        return 0;
    }

    public void updateArticleEntity(long id, ArticleEntity article) {
        try {
            articleMapper.updateArticle(id, article);
        } catch (Exception e) {
            logger.error("articleMapper.updateArticle error, id={}, article={}, e:", id, article, e);
        }
    }

    public List<ArticleEntity> getRecentEditArticles(int length) {
        try {
            return articleMapper.getRecentEditArticleList(length);
        } catch (Exception e) {
            logger.error("articleMapper.getRecentEditArticleList error, length={}, e:", length, e);
        }
        return null;
    }

    public List<ArticleEntity> getRecentCreateArticles(Pager pager) {
        try {
            return articleMapper.getRecentCreateArticleList(pager);
        } catch (Exception e) {
            logger.error("articleMapper.getRecentCreateArticleList error, pager={}, e:", pager, e);
        }
        return null;
    }

    public int getArticleCount() {
        try {
            return articleMapper.getArticleCount();
        } catch (Exception e) {
            logger.error("articleMapper.getArticleCount error, e:", e);
        }
        return 0;
    }

    /*
    tag
     */

    public List<TagEntity> getAllTagEntityList() {
        try {
            return tagMapper.getAllTagList();
        } catch (Exception e) {
            logger.error("tagMapper.getAllTagList error, e:", e);
        }
        return null;
    }

    public int saveTagEntity(TagEntity tag) {
        try {
            tagMapper.saveTag(tag);
            return tag.getId();
        } catch (Exception e) {
            logger.error("tagMapper.saveTag error, tag={}, e:", tag, e);
        }
        return 0;
    }

    public void updateTagEntity(int id, TagEntity tag) {
        try {
            tagMapper.updateTag(id, tag);
        } catch (Exception e) {
            logger.error("tagMapper.updateTag error, id={}, tag={}, e:", id, tag, e);
        }
    }

}
