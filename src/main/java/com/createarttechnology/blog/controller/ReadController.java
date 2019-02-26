package com.createarttechnology.blog.controller;

import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.service.ReadService;
import com.createarttechnology.blog.template.BaseTemplate;
import com.createarttechnology.blog.util.CollectionUtil;
import com.createarttechnology.logger.Logger;
import com.google.common.collect.Iterables;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Controller
public class ReadController {

    private static final Logger logger = Logger.getLogger(ReadController.class);

    private static final String PAGE_TITLE_SUFFIX = "_C.A.T";

    @Resource
    private ReadService readService;

    @RequestMapping(value = {"", "/index", "/list"}, method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model) {
        return list(0, request, model);
    }

    @RequestMapping(value = "/list/{tagId}", method = RequestMethod.GET)
    public String list(@PathVariable(value = "tagId") int tagId, HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        List<ListItem> itemList;
        if (tagId > 0) {
            itemList = readService.getListItemList(tagId);
        } else {
            itemList = readService.getRecentCreateListItemList(10);
        }
        List<Tag> path = readService.getPath(tagId);
        if (CollectionUtil.isNotEmpty(path)) {
            for (int i = 0; i < path.size(); i++) {
                model.addAttribute("tag_" + i, path.get(i));
            }
            tpl.setTitle(Iterables.getLast(path).getName() + PAGE_TITLE_SUFFIX);
        } else {
            model.addAttribute("tag_0", new Tag().setName("最新文章"));
            tpl.setTitle("最新文章" + PAGE_TITLE_SUFFIX);
        }
        model.addAttribute("list", itemList);
        model.addAttribute("page", tpl);
        return "page/list";
    }

    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    public String article(@PathVariable(value = "id") long id, HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        Article article = readService.getArticle(id);
        if (article != null) {
            tpl.setTitle(article.getTitle() + PAGE_TITLE_SUFFIX);
            List<Tag> path = article.getTags();
            if (CollectionUtil.isNotEmpty(path)) {
                for (int i = 0; i < path.size(); i++) {
                    model.addAttribute("tag_" + i, path.get(i));
                }
            } else {
                model.addAttribute("tag_0", new Tag().setName("未分类"));
            }
        }
        model.addAttribute("article", article);
        model.addAttribute("page", tpl);
        return "page/article/article";
    }

    @RequestMapping(value = "/article/form", method = RequestMethod.GET)
    public String articlePublish(HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        tpl.setTitle("发布文章" + PAGE_TITLE_SUFFIX);
        model.addAttribute("page", tpl);
        return "page/article/form";
    }

    @RequestMapping(value = "/article/{id}/update", method = RequestMethod.GET)
    public String articleUpdate(@PathVariable(value = "id") long id, HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        Article article = readService.getArticle(id);
        if (article != null) {
            tpl.setTitle(article.getTitle() + PAGE_TITLE_SUFFIX);
        }
        model.addAttribute("article", article);
        model.addAttribute("page", tpl);
        return "page/article/form";
    }

    @RequestMapping(value = "/private-login", method = RequestMethod.GET)
    public String privateLogin(HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        tpl.setTitle("登录" + PAGE_TITLE_SUFFIX);
        model.addAttribute("page", tpl);
        return "page/login";
    }



}
