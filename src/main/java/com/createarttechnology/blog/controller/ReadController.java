package com.createarttechnology.blog.controller;

import com.createarttechnology.blog.bean.Pager;
import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.ListItemList;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.service.ReadService;
import com.createarttechnology.blog.service.RedisService;
import com.createarttechnology.blog.template.BaseTemplate;
import com.createarttechnology.logger.Logger;
import com.google.common.collect.Iterables;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Controller
public class ReadController {

    private static final Logger logger = Logger.getLogger(ReadController.class);

    private static final int PAGE_SIZE = 5;

    @Resource
    private ReadService readService;
    @Resource
    private RedisService redisService;

    /**
     * 首页，最新文章
     */
    @RequestMapping(value = {"", "/index", "/list"}, method = RequestMethod.GET)
    public String index(@RequestParam(defaultValue = "1") int page, HttpServletRequest request, HttpServletResponse response, Model model) {
        BaseTemplate tpl = new BaseTemplate(request, response);
        Pager pager = new Pager(page, PAGE_SIZE);
        ListItemList data = readService.getRecentCreateListItemList(pager);
        tpl.setTitle("最新文章");
        model.addAttribute("list", data.getList());
        model.addAttribute("data", data);
        model.addAttribute("page", tpl);
        return "page/list";
    }

    /**
     * 根据tag id查看列表
     */
    @RequestMapping(value = "/list/{tagId}", method = RequestMethod.GET)
    public String list(@PathVariable(value = "tagId") int tagId, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (tagId <= 0) {
            return "redirect:/";
        }
        BaseTemplate tpl = new BaseTemplate(request, response);
        List<Tag> path = readService.getPath(tagId);
        if (CollectionUtils.isEmpty(path)) {
            return "redirect:/";
        }
        tpl.setCurrentTagPath(path);
        tpl.setTitle(Iterables.getLast(path).getName());
        List<ListItem> itemList = readService.getListItemList(tagId);
        model.addAttribute("list", itemList);
        model.addAttribute("page", tpl);
        return "page/list";
    }

    /**
     * 根据article id查看文章
     */
    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    public String article(@PathVariable(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) {
        BaseTemplate tpl = new BaseTemplate(request, response);
        Article article = readService.getArticle(id);
        if (article != null) {
            tpl.setTitle(article.getTitle());
            tpl.setCurrentTagPath(article.getTags());
            // 这里记一下除了我自己以外的pv
            if (!tpl.isAdmin()) {
                redisService.incrPv(id);
                article.setPv(article.getPv() + 1);
            }
        }
        model.addAttribute("article", article);
        model.addAttribute("page", tpl);
        return "page/article/article";
    }

    /**
     * 文章写入form页
     */
    @RequestMapping(value = "/article/form", method = RequestMethod.GET)
    public String articlePublish(HttpServletRequest request, HttpServletResponse response, Model model) {
        BaseTemplate tpl = new BaseTemplate(request, response);
        tpl.setTitle("发布文章");
        model.addAttribute("page", tpl);
        return "page/article/form";
    }

    /**
     * 更新文章form页
     */
    @RequestMapping(value = "/article/{id}/update", method = RequestMethod.GET)
    public String articleUpdate(@PathVariable(value = "id") long id, HttpServletRequest request, HttpServletResponse response, Model model) {
        BaseTemplate tpl = new BaseTemplate(request, response);
        Article article = readService.getArticle(id);
        if (article != null) {
            tpl.setTitle("更新文章 " + article.getTitle());
        }
        model.addAttribute("article", article);
        model.addAttribute("page", tpl);
        return "page/article/form";
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/private-login", method = RequestMethod.GET)
    public String privateLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
        BaseTemplate tpl = new BaseTemplate(request, response);
        tpl.setTitle("登录");
        model.addAttribute("page", tpl);
        return "page/login";
    }

}
