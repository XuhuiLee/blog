package com.createarttechnology.blog.controller;

import java.io.IOException;

import javax.annotation.Resource;

import com.createarttechnology.blog.service.SiteMapService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 站点地图
 * @author lixuhui
 */
@Controller
public class SiteMapController {

    @Resource
    private SiteMapService siteMapService;

    @RequestMapping(value = "/sitemap_baidu.xml", produces = "application/xml")
    @ResponseBody
    public String getSiteMapForBaidu() throws IOException {
        return siteMapService.getSiteMapForBaidu();
    }

    @RequestMapping(value = "/sitemap_google.xml", produces = "application/xml")
    @ResponseBody
    public String getSiteMapForGoogle() throws IOException {
        return siteMapService.getSiteMapForGoogle();
    }

}
