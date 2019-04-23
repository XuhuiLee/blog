package com.createarttechnology.blog.util;

import com.alibaba.fastjson.JSON;
import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.dao.entity.TagEntity;
import com.createarttechnology.jutil.StringUtil;

import java.util.List;
import java.util.function.Function;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class Converter {

    /**
     * ArticleEntity - Article
     */
    public static Article articleEntity2Article(ArticleEntity input) {
        if (input == null) {
            return null;
        }

        Article output = new Article();
        output.setId(input.getId());
        output.setTitle(input.getTitle());
        output.setRichContent(input.getRichContent());
        output.setCreateTime(TimeUtil.getTimeStringInt(input.getCreateTime()));
        output.setUpdateTime(TimeUtil.getTimeStringInt(input.getUpdateTime()));
        output.setMarkdown(input.getMarkdown());

        return output;
    }

    /**
     * ArticleEntity - ListItem
     */
    public static ListItem articleEntity2ListItem(ArticleEntity input) {
        if (input == null) {
            return null;
        }

        ListItem output = new ListItem();
        output.setId(input.getId());
        output.setTitle(input.getTitle());
        output.setSimpleContent(RichTextUtil.formatSimpleText(input.getSimpleContent()));
        if (StringUtil.isNotEmpty(input.getPics())) {
            output.setPics(JSON.parseArray(input.getPics(), String.class));
        }
        output.setCreateTime(TimeUtil.getTimeStringInt(input.getCreateTime()));
        output.setUpdateTime(TimeUtil.getTimeStringInt(input.getUpdateTime()));
        output.setTagId(input.getTag());

        return output;
    }

    /**
     * ArticleEntity List - ListItem List
     */
    public static List<ListItem> articleEntityList2ListItemList(List<ArticleEntity> input) {
        return CollectionUtil.transformList(input, new Function<ArticleEntity, ListItem>() {
            @Override
            public ListItem apply(ArticleEntity articleEntity) {
                return articleEntity2ListItem(articleEntity);
            }
        });
    }

    /**
     * TagEntity - Tag
     */
    public static Tag tagEntity2Tag(TagEntity input) {
        if (input == null) {
            return null;
        }

        Tag tag = new Tag();
        tag.setId(input.getId());
        tag.setParentId(input.getParentId());
        tag.setName(input.getName());

        return tag;
    }

    /**
     * TagEntity List - Tag List
     */
    public static List<Tag> tagEntityList2TagList(List<TagEntity> input) {
        return CollectionUtil.transformList(input, new Function<TagEntity, Tag>() {
            @Override
            public Tag apply(TagEntity tagEntity) {
                return tagEntity2Tag(tagEntity);
            }
        });
    }

}
