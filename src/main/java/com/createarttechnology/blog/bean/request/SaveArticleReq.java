package com.createarttechnology.blog.bean.request;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class SaveArticleReq {
    private long id;
    private String title;
    private String content;
    private int tag;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "SaveArticleReq{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", tag=" + tag +
                '}';
    }
}
