package com.createarttechnology.blog.controller;

import com.createarttechnology.blog.bean.request.SaveArticleReq;
import com.createarttechnology.blog.bean.request.SaveTagReq;
import com.createarttechnology.blog.service.LoginService;
import com.createarttechnology.blog.service.WriteService;
import com.createarttechnology.blog.template.BaseTemplate;
import com.createarttechnology.blog.util.Checker;
import com.createarttechnology.common.BaseResp;
import com.createarttechnology.common.ErrorInfo;
import com.createarttechnology.jutil.AntiBotUtil;
import com.createarttechnology.logger.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lixuhui on 2018/9/13.
 */
@RestController
public class WriteController {

    private static final Logger logger = Logger.getLogger(WriteController.class);

    @Resource
    private WriteService writeService;
    @Resource
    private LoginService loginService;

    /**
     * 保存或修改文章
     */
    @RequestMapping(value = "/article/{action}", method = RequestMethod.POST)
    public BaseResp article(@PathVariable("action") String action, @RequestBody SaveArticleReq req, HttpServletRequest request, HttpServletResponse response) {
        logger.info("article, action={}, req={}", action, req);
        BaseResp resp = new BaseResp();

        BaseTemplate tpl = new BaseTemplate(request, response);
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
        resp = Checker.checkSaveArticleReq(req, modify);
        if (!resp.success()) {
            return resp;
        }
        return writeService.saveArticle(req, modify);
    }

    /**
     * 保存或修改tag
     */
    @RequestMapping(value = "/tag/{action}", method = RequestMethod.POST)
    public BaseResp tag(@PathVariable("action") String action, @RequestBody SaveTagReq req, HttpServletRequest request, HttpServletResponse response) {
        logger.info("tag, action={}, req={}", action, req);
        BaseResp resp = new BaseResp();

        BaseTemplate tpl = new BaseTemplate(request, response);
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
        resp = Checker.checkSaveTagReq(req, modify);
        if (!resp.success()) {
            return resp;
        }
        return writeService.saveTag(req, modify);
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResp login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        logger.info("login, username={}, password={}", username, password);
        if (AntiBotUtil.isBot(request)) {
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return null;
            } catch (IOException ignored) {}
        }
        return loginService.login(response, username, password);
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public BaseResp logout(HttpServletResponse response) {
        return loginService.logout(response);
    }
}
