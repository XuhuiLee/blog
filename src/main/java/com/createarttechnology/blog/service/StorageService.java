package com.createarttechnology.blog.service;

import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.dao.entity.TagEntity;
import com.createarttechnology.blog.dao.mapper.ArticleMapper;
import com.createarttechnology.blog.dao.mapper.TagMapper;
import com.createarttechnology.jutil.log.Logger;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Service
public class StorageService {

    private static final Logger logger = Logger.getLogger(StorageService.class);

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private TagMapper tagMapper;

    public ArticleEntity getArticleEntity(long id) {
        try {
            return articleMapper.getArticle(id);
        } catch (Exception e) {
            logger.error("articleMapper.getArticle error, id={}, e:", id, e);
        }
        return null;
    }

    public List<ArticleEntity> getArticleEntityList() {
        try {
            return articleMapper.getArticleList();
        } catch (Exception e) {
            logger.error("articleMapper.getArticleList error, e:", e);
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

    public TagEntity getTagEntity(int id) {
        try {
            return tagMapper.getTag(id);
        } catch (Exception e) {
            logger.error("tagMapper.getTag error, id={}, e:", id, e);
        }
        return null;
    }

    public List<TagEntity> getTagEntityList(List<Integer> ids) {
        try {
            return tagMapper.getTagList(Joiner.on(',').join(ids));
        } catch (Exception e) {
            logger.error("tagMapper.getTagList error, ids={}, e:", ids, e);
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
