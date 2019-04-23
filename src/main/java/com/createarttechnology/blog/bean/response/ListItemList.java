package com.createarttechnology.blog.bean.response;

import com.createarttechnology.blog.bean.Pager;

import java.util.List;

/**
 * Created by lixuhui on 2019/4/23.
 */
public class ListItemList {
    private Pager pager;
    private int total;
    private List<ListItem> data;

    public ListItemList(Pager pager, int total, List<ListItem> data) {
        this.pager = pager;
        this.total = total;
        this.data = data;
    }

    public int getPageNo() {
        return pager.getPage();
    }

    public int getTotalPage() {
        int total = this.total / pager.getLimit();
        if (this.total % pager.getLimit() != 0) {
            total ++;
        }
        return total;
    }

    public List<ListItem> getList() {
        return data;
    }

    @Override
    public String toString() {
        return "ListItemList{" +
                "pager=" + pager +
                ", total=" + total +
                ", data=" + data +
                '}';
    }
}
