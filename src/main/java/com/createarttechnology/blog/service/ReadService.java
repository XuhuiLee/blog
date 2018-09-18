package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.util.CollectionUtil;
import com.createarttechnology.blog.util.Converter;
import com.createarttechnology.jutil.StringUtil;
import com.createarttechnology.jutil.log.Logger;
import com.google.common.base.Splitter;
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
    @Resource
    private TagService tagService;

    public Article getArticle(long id) {
        ArticleEntity entity = storageService.getArticleEntity(id);
        Article article = Converter.articleEntity2Article(entity);
        if (StringUtil.isNotEmpty(entity.getTags())) {
            List<Integer> tagIds = CollectionUtil.transformList(Splitter.on(',').omitEmptyStrings().trimResults().splitToList(entity.getTags()), s -> StringUtil.convertInt(s, 0));
            List<Tag> tags = tagService.getTagParentPathList(tagIds);
            article.setTags(tags);
        }
        return article;
    }

    public List<ListItem> getListItemList() {
        List<ArticleEntity> entity = storageService.getArticleEntityList();
        return Converter.articleEntityList2ListItemList(entity);
    }


}
