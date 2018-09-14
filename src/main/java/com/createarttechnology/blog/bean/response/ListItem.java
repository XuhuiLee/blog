package com.createarttechnology.blog.bean.response;

import java.util.List;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class ListItem {
    private long id;
    private String title;
    private String simpleContent;
    private List<String> pics;
    private String createTime;
    private String updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSimpleContent() {
        return simpleContent;
    }

    public void setSimpleContent(String simpleContent) {
        this.simpleContent = simpleContent;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", simpleContent='" + simpleContent + '\'' +
                ", pics=" + pics +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
