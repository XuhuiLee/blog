package com.createarttechnology.blog.template;

import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.service.ReadService;
import com.createarttechnology.blog.service.TagService;
import com.createarttechnology.constant.StringConstant;
import com.createarttechnology.jutil.CookieUtil;
import com.createarttechnology.jutil.StringUtil;
import com.createarttechnology.supporter.redis.LoginClient;
import org.apache.commons.lang.math.RandomUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Lee on 2018/9/15.
 */
public class BaseTemplate {
    private HttpServletRequest request;
    private HttpServletResponse response;

    private long uid;
    private long uuid;
    private boolean login;

    private String title = "CreateArtTechnology/blog";
    private boolean admin = false;
    private List<Tag> currentTagPath;

    private long totalPv;
    private int totalArticle;

    public BaseTemplate(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        init();
    }

    private void init() {
        String lk = CookieUtil.getCookie(request, StringConstant.COOKIE_LOGIN_KEY);
        String ltk = CookieUtil.getCookie(request, StringConstant.COOKIE_LOGIN_TOKEN);
        String uuid = CookieUtil.getCookie(request, StringConstant.COOKIE_USER_UUID);

        // 设置uuid
        this.uuid = StringUtil.convertLong(uuid, 0);
        if (this.uuid <= 0) {
            this.uuid = System.currentTimeMillis() + RandomUtils.nextInt(1000);
            // 保存三个月
            CookieUtil.addCookie(response, StringConstant.COOKIE_USER_UUID, String.valueOf(this.uuid), 7776000);
        }

        // 设置登录相关信息
        long lkNum = StringUtil.convertLong(lk, 0);
        if (lkNum > 0 && StringUtil.isNotEmpty(ltk)) {
            this.login = LoginClient.checkLogin(lkNum, ltk);
            if (this.login) {
                this.uid = lkNum;
                // 管理员
                if (uid == 1) {
                    this.admin = true;
                }
            }
        }
    }

    public long getUid() {
        return uid;
    }

    public long getUuid() {
        return uuid;
    }

    public boolean isLogin() {
        return login;
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
        this.currentTagPath = currentTagPath;
    }

    public void setTotalPv(long totalPv) {
        this.totalPv = totalPv;
    }

    public long getTotalPv() {
        return totalPv;
    }

    public void setTotalArticle(int totalArticle) {
        this.totalArticle = totalArticle;
    }

    public int getTotalArticle() {
        return totalArticle;
    }
}
