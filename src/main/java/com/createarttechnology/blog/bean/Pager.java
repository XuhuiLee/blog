package com.createarttechnology.blog.bean;

/**
 * Created by lixuhui on 2019/4/23.
 */
public class Pager {
    private int page;
    private int pageSize;

    public Pager(int page, int pageSize) {
        this.page = page < 1 ? 1 : page;
        this.pageSize = pageSize;
    }

    public int getStart() {
        return (this.page - 1) * pageSize;
    }

    public int getLimit() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
