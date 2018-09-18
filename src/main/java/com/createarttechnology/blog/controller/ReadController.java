package com.createarttechnology.blog.controller;

import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.service.ReadService;
import com.createarttechnology.blog.template.BaseTemplate;
import com.createarttechnology.jutil.log.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Controller
public class ReadController {

    private static final Logger logger = Logger.getLogger(ReadController.class);

    @Resource
    private ReadService readService;

    @RequestMapping(value = {"/", "/list"})
    public String list(HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        List<ListItem> itemList = readService.getListItemList();
        model.addAttribute("list", itemList);
        model.addAttribute("page", tpl);
        return "page/list";
    }

    @RequestMapping("/article/{id}")
    public String article(@PathVariable(value = "id") long id, HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        Article article = readService.getArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("page", tpl);
        return "page/article/article";
    }

    @RequestMapping("/article/form")
    public String articlePublish(HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        model.addAttribute("page", tpl);
        return "page/article/form";
    }

    @RequestMapping("/article/update")
    public String articleUpdate(@RequestParam(value = "id", required = false, defaultValue = "0") long id, HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        Article article = readService.getArticle(id);
        model.addAttribute("article", article);
        model.addAttribute("page", tpl);
        return "page/article/form";
    }

    @RequestMapping("/private-login")
    public String privateLogin(HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        model.addAttribute("page", tpl);
        return "page/login";
    }



}
