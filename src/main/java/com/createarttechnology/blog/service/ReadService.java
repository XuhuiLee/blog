package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.util.CollectionUtil;
import com.createarttechnology.blog.util.Converter;
import com.createarttechnology.logger.Logger;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/14.
 */
@Service
public class ReadService {

    private static final Logger logger = Logger.getLogger(ReadService.class);

    private static final List<Tag> NO_TAG_LIST = Lists.newArrayList(new Tag().setName("未分类"));

    @Resource
    private StorageService storageService;
    @Resource
    private TagService tagService;

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
        List<ArticleEntity> entity = storageService.getArticleEntityList(tagId);
        return Converter.articleEntityList2ListItemList(entity);
    }

    public List<Integer> getMenuIdList(int tagId) {
        List<Tag> path = tagService.getTagParentPath(tagId);
        return CollectionUtil.transformList(path, Tag::getId);
    }

    public List<Tag> getPath(int tagId) {
        return tagService.getTagParentPath(tagId);
    }
}
