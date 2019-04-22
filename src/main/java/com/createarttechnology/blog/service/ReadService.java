package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.util.CollectionUtil;
import com.createarttechnology.blog.util.Converter;
import com.createarttechnology.logger.Logger;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lixuhui on 2018/9/14.
 */
@Service
public class ReadService {

    private static final Logger logger = Logger.getLogger(ReadService.class);

    private static final List<Tag> NO_TAG_LIST = Lists.newArrayList(new Tag().setName("未分类"));

    private static LoadingCache<Integer, List<ListItem>> recentArticleCache = null;

    @Resource
    private StorageService storageService;
    @Resource
    private TagService tagService;

    @PostConstruct
    public void init() {
        recentArticleCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<Integer, List<ListItem>>() {
                    @Override
                    public List<ListItem> load(Integer length) {
                        List<ArticleEntity> entities = storageService.getRecentEditArticles(length);
                        return Converter.articleEntityList2ListItemList(entities);
                    }
                });
    }

    public Article getArticle(long id) {
        ArticleEntity entity = storageService.getArticleEntity(id);
        Article article = Converter.articleEntity2Article(entity);
        if (entity.getTag() > 0) {
            List<Tag> tags = tagService.getTagParentPath(entity.getTag());
            article.setTags(tags);
        } else {
            article.setTags(NO_TAG_LIST);
        }
        return article;
    }

    public List<ListItem> getListItemList(int tagId) {
        List<ArticleEntity> entities = storageService.getArticleEntityList(tagId);
        List<ListItem> result = Converter.articleEntityList2ListItemList(entities);
        fillTags(result);
        return result;
    }

    public List<ListItem> getRecentCreateListItemList(int length) {
        List<ArticleEntity> entities = storageService.getRecentCreateArticles(length);
        List<ListItem> result = Converter.articleEntityList2ListItemList(entities);
        fillTags(result);
        return result;
    }

    public List<Integer> getMenuIdList(int tagId) {
        List<Tag> path = tagService.getTagParentPath(tagId);
        return CollectionUtil.transformList(path, Tag::getId);
    }

    public static List<ListItem> getListItem(int length) {
        return recentArticleCache == null ? null : recentArticleCache.getUnchecked(length);
    }

    public List<Tag> getPath(int tagId) {
        return tagService.getTagParentPath(tagId);
    }

    private void fillTags(List<ListItem> input) {
        if (CollectionUtil.isNotEmpty(input)) {
            for (ListItem item : input) {
                if (item.getTagId() == 0) {
                    item.setTags(NO_TAG_LIST);
                } else {
                    item.setTags(tagService.getTagParentPath(item.getTagId()));
                }
            }
        }
    }
}
