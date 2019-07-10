package com.createarttechnology.blog.template;

import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.service.ReadService;
import com.createarttechnology.blog.service.TagService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Lee on 2018/9/15.
 */
public class BaseTemplate {
    private HttpServletRequest request;

    private String title = "CreateArtTechnology/blog";
    private boolean admin = false;
    private List<Tag> currentTagPath;

    public BaseTemplate(HttpServletRequest request) {
        this.request = request;
        init();
    }

    private void init() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("cat_blog_pass".equals(cookie.getName())) {
                    admin = true;
                }
            }
        }
    }

    public boolean isAdmin() {
        return admin;
    }

    public List<Tag> getTopTags() {
        return TagService.getTopTagList();
    }

    public Tag getTag(int tagId) {
        return TagService.getTag(tagId);
    }

    public List<ListItem> getRecentEditArticles() {
        return ReadService.getListItem(5);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Tag> getCurrentTagPath() {
        return currentTagPath;
    }

    public void setCurrentTagPath(List<Tag> currentTagPath) {
        System.out.println(currentTagPath);
        this.currentTagPath = currentTagPath;
    }
}
