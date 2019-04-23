package com.createarttechnology.blog.controller;

import com.createarttechnology.blog.bean.request.SaveArticleReq;
import com.createarttechnology.blog.bean.request.SaveTagReq;
import com.createarttechnology.blog.bean.response.BaseResp;
import com.createarttechnology.blog.constants.ErrorInfo;
import com.createarttechnology.blog.service.ConfigService;
import com.createarttechnology.blog.service.WriteService;
import com.createarttechnology.blog.template.BaseTemplate;
import com.createarttechnology.blog.util.Checker;
import com.createarttechnology.logger.Logger;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lixuhui on 2018/9/13.
 */
@RestController
public class WriteController {

    private static final Logger logger = Logger.getLogger(WriteController.class);

    @Resource
    private WriteService writeService;
    @Resource
    private ConfigService configService;

    /**
     * 保存或修改文章
     */
    @RequestMapping(value = "/article/{action}", method = RequestMethod.POST)
    public BaseResp article(@PathVariable("action") String action, @RequestBody SaveArticleReq req, HttpServletRequest request) {
        logger.info("article, action={}, req={}", action, req);
        BaseResp resp = new BaseResp();

        BaseTemplate tpl = new BaseTemplate(request);
        if (!tpl.isAdmin()) {
            return resp.setErrorInfo(ErrorInfo.NO_AUTH);
        }

        boolean modify = false;
        switch (action) {
            case "update":
                modify = true;
            case "save":
                break;
            default:
                return resp.setErrorInfo(ErrorInfo.INVALID_PARAMS);
        }
        if (modify && req.getId() <= 0) {
            return resp.setErrorInfo(ErrorInfo.INVALID_PARAMS);
        }
        if (!modify && req.getId() > 0) {
            return resp.setErrorInfo(ErrorInfo.INVALID_PARAMS);
        }
        resp = Checker.checkSaveArticleReq(req);
        if (!resp.success()) {
            return resp;
        }
        return writeService.saveArticle(req, modify);
    }

    /**
     * 保存或修改tag
     */
    @RequestMapping(value = "/tag/{action}", method = RequestMethod.POST)
    public BaseResp tag(@PathVariable("action") String action, @RequestBody SaveTagReq req, HttpServletRequest request) {
        logger.info("tag, action={}, req={}", action, req);
        BaseResp resp = new BaseResp();

        BaseTemplate tpl = new BaseTemplate(request);
        if (!tpl.isAdmin()) {
            return resp.setErrorInfo(ErrorInfo.NO_AUTH);
        }

        boolean modify = false;
        switch (action) {
            case "update":
                modify = true;
            case "save":
                break;
            default:
                return resp.setErrorInfo(ErrorInfo.INVALID_PARAMS);
        }
        if (modify && req.getId() <= 0) {
            return resp.setErrorInfo(ErrorInfo.INVALID_PARAMS);
        }
        if (!modify && req.getId() > 0) {
            return resp.setErrorInfo(ErrorInfo.INVALID_PARAMS);
        }
        resp = Checker.checkSaveTagReq(req);
        if (!resp.success()) {
            return resp;
        }
        return writeService.saveTag(req, modify);
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResp login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) {
        logger.info("login, username={}, password={}", username, password);
        BaseResp resp = new BaseResp();

        logger.info("password={}, md5={}", password, DigestUtils.md5DigestAsHex("password".getBytes()));
        if (configService.getUsername().equals(username) && DigestUtils.md5DigestAsHex(configService.getPassword().getBytes()).equalsIgnoreCase(password)) {
            Cookie cookie = new Cookie("cat_blog_pass", "1");
            response.addCookie(cookie);
            return resp.setErrorInfo(ErrorInfo.SUCCESS);
        }
        return resp.setErrorInfo(ErrorInfo.NO_AUTH);
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public BaseResp logout(HttpServletResponse response) {
        BaseResp resp = new BaseResp();

        Cookie cookie = new Cookie("cat_blog_pass", "1");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return resp.setErrorInfo(ErrorInfo.SUCCESS);
    }
}
