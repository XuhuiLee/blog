package com.createarttechnology.blog.controller;

import com.createarttechnology.blog.bean.Pager;
import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.ListItemList;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.service.ReadService;
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
import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Controller
public class ReadController {

    private static final Logger logger = Logger.getLogger(ReadController.class);

    private static final String PAGE_TITLE_SUFFIX = " - C.A.T";
    private static final int PAGE_SIZE = 2;

    @Resource
    private ReadService readService;

    /**
     * 首页，最新文章
     */
    @RequestMapping(value = {"", "/index", "/list"}, method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model, @RequestParam(defaultValue = "1") int page) {
        BaseTemplate tpl = new BaseTemplate(request);
        Pager pager = new Pager(page, PAGE_SIZE);
        ListItemList data = readService.getRecentCreateListItemList(pager);
        model.addAttribute("tag_0", new Tag().setName("最新文章"));
        tpl.setTitle("最新文章" + PAGE_TITLE_SUFFIX);
        model.addAttribute("list", data.getList());
        model.addAttribute("data", data);
        model.addAttribute("page", tpl);
        return "page/list";
    }

    /**
     * 根据tag id查看列表
     */
    @RequestMapping(value = "/list/{tagId}", method = RequestMethod.GET)
    public String list(@PathVariable(value = "tagId") int tagId, HttpServletRequest request, Model model) {
        if (tagId <= 0) {
            return "redirect:/";
        }
        BaseTemplate tpl = new BaseTemplate(request);
        List<Tag> path = readService.getPath(tagId);
        if (CollectionUtils.isEmpty(path)) {
            return "redirect:/";
        }
        for (int i = 0; i < path.size(); i++) {
            model.addAttribute("tag_" + i, path.get(i));
        }
        tpl.setTitle(Iterables.getLast(path).getName() + PAGE_TITLE_SUFFIX);
        List<ListItem> itemList = readService.getListItemList(tagId);
        model.addAttribute("list", itemList);
        model.addAttribute("page", tpl);
        return "page/list";
    }

    /**
     * 根据article id查看文章
     */
    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
    public String article(@PathVariable(value = "id") long id, HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        Article article = readService.getArticle(id);
        if (article != null) {
            tpl.setTitle(article.getTitle() + PAGE_TITLE_SUFFIX);
            List<Tag> path = article.getTags();
            if (CollectionUtils.isNotEmpty(path)) {
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

    /**
     * 文章写入form页
     */
    @RequestMapping(value = "/article/form", method = RequestMethod.GET)
    public String articlePublish(HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        tpl.setTitle("发布文章" + PAGE_TITLE_SUFFIX);
        model.addAttribute("page", tpl);
        return "page/article/form";
    }

    /**
     * 更新文章form页
     */
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

    /**
     * 登录
     */
    @RequestMapping(value = "/private-login", method = RequestMethod.GET)
    public String privateLogin(HttpServletRequest request, Model model) {
        BaseTemplate tpl = new BaseTemplate(request);
        tpl.setTitle("登录" + PAGE_TITLE_SUFFIX);
        model.addAttribute("page", tpl);
        return "page/login";
    }



}
