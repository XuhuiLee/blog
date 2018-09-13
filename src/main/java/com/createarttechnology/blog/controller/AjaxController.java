package com.createarttechnology.blog.controller;

import com.createarttechnology.blog.bean.Article;
import com.createarttechnology.blog.bean.BaseResp;
import com.createarttechnology.blog.service.ArticleService;
import com.createarttechnology.jutil.log.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by lixuhui on 2018/9/13.
 */
@RestController
public class AjaxController {

    private static final Logger logger = Logger.getLogger(AjaxController.class);

    @Resource
    private ArticleService articleService;

    @RequestMapping(value = "/article/publish", method = RequestMethod.POST)
    public BaseResp saveArticle(@RequestBody Article article) {
        logger.info("saveArticle, article={}", article);
        if (article.getId() == 0) {
            long id = articleService.saveArticle(article);
        } else {
            articleService.updateArticle(article);
        }
        BaseResp<Long> resp = new BaseResp<Long>();
        resp.setRetCode(0);
        resp.setMsg("success");
        resp.setData(article.getId());
        return resp;
    }

}
