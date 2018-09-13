package com.createarttechnology.blog.bean;

/**
 * Created by lixuhui on 2018/9/13.
 */
public class Article {
    private long id;
    private long authorId;
    private String title;
    private String richContent;
    private String simpleContent;
    private String tags;
    private String pics;
    private String videos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRichContent() {
        return richContent;
    }

    public void setRichContent(String richContent) {
        this.richContent = richContent;
    }

    public String getSimpleContent() {
        return simpleContent;
    }

    public void setSimpleContent(String simpleContent) {
        this.simpleContent = simpleContent;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", title='" + title + '\'' +
                ", richContent='" + richContent + '\'' +
                ", simpleContent='" + simpleContent + '\'' +
                ", tags='" + tags + '\'' +
                ", pics='" + pics + '\'' +
                ", videos='" + videos + '\'' +
                '}';
    }
}
