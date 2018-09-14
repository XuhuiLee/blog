package com.createarttechnology.blog.util;

import com.alibaba.fastjson.JSON;
import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.dao.entity.TagEntity;
import com.createarttechnology.jutil.StringUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class Converter {

    public static Article articleEntity2Article(ArticleEntity input) {
        if (input == null) {
            return null;
        }

        Article output = new Article();
        output.setId(input.getId());
        output.setTitle(input.getTitle());
        output.setRichContent(input.getRichContent());
        output.setCreateTime(TimeUtil.getTimeString(input.getCreateTime()));
        output.setUpdateTime(TimeUtil.getTimeString(input.getUpdateTime()));

        return output;
    }

    public static ListItem articleEntity2ListItem(ArticleEntity input) {
        if (input == null) {
            return null;
        }

        ListItem output = new ListItem();
        output.setId(input.getId());
        output.setTitle(input.getTitle());
        output.setSimpleContent(input.getSimpleContent());
        if (StringUtil.isNotEmpty(input.getPics())) {
            output.setPics(JSON.parseArray(input.getPics(), String.class));
        }
        output.setCreateTime(TimeUtil.getTimeString(input.getCreateTime()));
        output.setUpdateTime(TimeUtil.getTimeString(input.getUpdateTime()));

        return output;
    }

    public static List<ListItem> articleEntityList2ListItemList(List<ArticleEntity> input) {
        if (CollectionUtils.isEmpty(input)) {
            return null;
        }

        List<ListItem> output = Lists.newArrayListWithCapacity(input.size());
        for (ArticleEntity in : input) {
            ListItem out = articleEntity2ListItem(in);
            if (out != null) {
                output.add(out);
            }
        }

        return output;
    }

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

    public static List<Tag> tagEntityList2TagList(List<TagEntity> input) {
        if (CollectionUtils.isEmpty(input)) {
            return null;
        }

        List<Tag> output = Lists.newArrayListWithCapacity(input.size());
        for (TagEntity in : input) {
            Tag out = tagEntity2Tag(in);
            if (out != null) {
                output.add(out);
            }
        }

        return output;
    }

}
