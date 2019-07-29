package com.createarttechnology.blog.bean.request;

/**
 * Created by lixuhui on 2019/7/29.
 */
public class GetArticleReq {
    private long id;
    private boolean incrPv;

    public long getId() {
        return id;
    }

    public GetArticleReq setId(long id) {
        this.id = id;
        return this;
    }

    public boolean isIncrPv() {
        return incrPv;
    }

    public GetArticleReq setIncrPv(boolean incrPv) {
        this.incrPv = incrPv;
        return this;
    }

    @Override
    public String toString() {
        return "GetArticleReq{" +
                "id=" + id +
                ", incrPv=" + incrPv +
                '}';
    }
}
