package com.createarttechnology.blog.bean.response;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class Tag {
    private int id;
    private int parentId;
    private String name;
    private int level;
    private List<Tag> subTags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Tag> getSubTags() {
        return subTags;
    }

    public void addSubTag(Tag subTag) {
        if (subTag != null) {
            if (subTags == null) {
                subTags = Lists.newArrayList();
            }
            subTags.add(subTag);
        }
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                '}';
    }
}
