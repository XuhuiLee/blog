package com.createarttechnology.blog.template;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Lee on 2018/9/15.
 */
public class BaseTemplate {
    private HttpServletRequest request;
    private HttpServletResponse response;

    private boolean admin;

    public BaseTemplate(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        init();
    }

    private void init() {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("cat_blog_pass".equals(cookie.getName()) && "pass".equals(cookie.getValue())) {
                    admin = true;
                }
            }
        }
    }

}
