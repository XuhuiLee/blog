package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.dao.entity.TagEntity;
import com.createarttechnology.blog.util.Converter;
import com.createarttechnology.jutil.log.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/14.
 */
@Service
public class ReadService {

    private static final Logger logger = Logger.getLogger(ReadService.class);

    @Resource
    private StorageService storageService;

    public Article getArticle(long id) {
        ArticleEntity entity = storageService.getArticleEntity(id);
        Article article = Converter.articleEntity2Article(entity);
        article.setTag(getTagFromId(entity.getTag()));
        return article;
    }

    public List<ListItem> getListItemList() {
        List<ArticleEntity> entity = storageService.getArticleEntityList();
        return Converter.articleEntityList2ListItemList(entity);
    }

    public Tag getTagFromId(int id) {
        TagEntity tagEntity = storageService.getTagEntity(id);
        return Converter.tagEntity2Tag(tagEntity);
    }
    public List<Tag> getTagListFromIds(List<Integer> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            List<TagEntity> tagEntities = storageService.getTagEntityList(ids);
            return Converter.tagEntityList2TagList(tagEntities);
        }
        return null;
    }

}
