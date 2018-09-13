package com.createarttechnology.blog.controller;

import com.createarttechnology.blog.bean.Article;
import com.createarttechnology.blog.service.ArticleService;
import com.createarttechnology.jutil.log.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/13.
 */
@Controller
public class PageController {

    private static final Logger logger = Logger.getLogger(PageController.class);

    @Resource
    private ArticleService articleService;

    @RequestMapping(value = {"/", "/list"})
    public String list(Model model) {
        List<Article> articleList = articleService.getArticleList();
        model.addAttribute("list", articleList);
        return "page/list";
    }

    @RequestMapping("/article/{id}")
    public String articl(@PathVariable(value = "id") long id, Model model) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        return "page/article";
    }

    @RequestMapping("/publish")
    public String publish(@RequestParam(value = "id", required = false, defaultValue = "0") long id, Model model) {
        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        return "page/publish";
    }
}
