package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.Pager;
import com.createarttechnology.blog.bean.request.GetArticleReq;
import com.createarttechnology.blog.bean.response.Article;
import com.createarttechnology.blog.bean.response.ListItem;
import com.createarttechnology.blog.bean.response.ListItemList;
import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.template.BaseTemplate;
import com.createarttechnology.blog.util.Converter;
import com.createarttechnology.jutil.CollectionUtil;
import com.createarttechnology.logger.Logger;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by lixuhui on 2018/9/14.
 */
@Service
public class ReadService {

    private static final Logger logger = Logger.getLogger(ReadService.class);
    private static final long FIVE_MIN = 5 * 60 * 1000;
    private static final long FIVE_SEC = 5 * 1000;

    private static final List<Tag> NO_TAG_LIST = Lists.newArrayList(new Tag().setName("未分类笔记"));

    private static LoadingCache<Integer, List<ListItem>> recentArticleCache = null;

    // 做一个简易Cache
    private static volatile long totalPvExpireTime;
    private static volatile long totalPvCache;

    // 做一个简易Cache
    private static volatile long totalArticleExpireTime;
    private static volatile int totalArticleCache;

    @Resource
    private StorageService storageService;
    @Resource
    private RedisService redisService;

    /**
     * 缓存最近编辑列表
     */
    @PostConstruct
    public void init() {
        recentArticleCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<Integer, List<ListItem>>() {
                    @Override
                    public List<ListItem> load(Integer length) {
                        List<ArticleEntity> entities = storageService.getRecentEditArticles(length);
                        return Converter.articleEntityList2ListItemList(entities);
                    }
                });
    }

    public BaseTemplate getTemplate(HttpServletRequest request, HttpServletResponse response) {
        BaseTemplate tpl = new BaseTemplate(request, response);
        tpl.setTotalPv(getTotalPv());
        tpl.setTotalArticle(getTotalArticle());
        return tpl;
    }

    /**
     * 文章详情
     */
    public Article getArticle(GetArticleReq req) {
        long id = req.getId();
        boolean incrPv = req.isIncrPv();

        ArticleEntity entity = storageService.getArticleEntity(id);
        Article article = Converter.articleEntity2Article(entity);
        if (article != null) {
            if (entity.getTag() > 0) {
                List<Tag> tags = TagService.getParentTagPath(entity.getTag());
                article.setTags(tags);
            } else {
                article.setTags(NO_TAG_LIST);
            }
            long pv;
            if (incrPv) {
                pv = redisService.incrPv(id);
            } else {
                pv = redisService.getPv(id);
            }
            article.setPv(pv);
        }
        return article;
    }

    /**
     * 普通列表页，不翻页
     */
    public List<ListItem> getListItemList(int tagId) {
        Set<Integer> tagIdPath = TagService.getChildTagIdPath(tagId);
        if (CollectionUtils.isEmpty(tagIdPath)) {
            return null;
        }
        List<ArticleEntity> entities = storageService.getArticleListByParentTagId(tagIdPath);
        List<ListItem> result = Converter.articleEntityList2ListItemList(entities);
        fillTags(result);
        fillPvs(result);
        return result;
    }

    /**
     * 首页列表，翻页
     */
    public ListItemList getRecentCreateListItemList(Pager pager) {
        int count = storageService.getArticleCount();
        List<ArticleEntity> entities = storageService.getRecentCreateArticles(pager);
        List<ListItem> result = Converter.articleEntityList2ListItemList(entities);
        fillTags(result);
        fillPvs(result);
        return new ListItemList(pager, count, result);
    }

    /**
     * 最新编辑列表
     */
    public static List<ListItem> getListItem(int length) {
        return recentArticleCache == null ? null : recentArticleCache.getUnchecked(length);
    }

    /**
     * 面包屑导航
     */
    public List<Tag> getPath(int tagId) {
        return TagService.getParentTagPath(tagId);
    }

    /**
     * sitemap
     */
    public List<ArticleEntity> getAllArticleIdAndUpdateTime() {
        return storageService.getArticleIdAndUpdateTime();
    }

    /**
     * 填充tag
     */
    private void fillTags(List<ListItem> input) {
        if (CollectionUtils.isNotEmpty(input)) {
            for (ListItem item : input) {
                if (item.getTagId() == 0) {
                    item.setTags(NO_TAG_LIST);
                } else {
                    item.setTags(TagService.getParentTagPath(item.getTagId()));
                }
            }
        }
    }

    /**
     * 填充pv
     */
    private void fillPvs(List<ListItem> input) {
        if (CollectionUtil.isEmpty(input)) {
            return;
        }

        List<Long> idList = CollectionUtil.transformList(input, ListItem::getId);
        Map<Long, Integer> pvMap = redisService.batchGetPv(idList);
        input.forEach(i -> {
            if (pvMap.containsKey(i.getId())) {
                i.setPv(pvMap.get(i.getId()));
            }
        });
    }

    /**
     * 简单缓存一下总pv
     */
    private long getTotalPv() {
        long now = System.currentTimeMillis();
        if (totalPvExpireTime < now) {
            synchronized (ReadService.class) {
                totalPvCache = redisService.getTotalPv();
                totalPvExpireTime = now + FIVE_SEC;
            }
        }
        return totalPvCache;
    }

    /**
     * 简单缓存一下总文章数
     */
    private int getTotalArticle() {
        long now = System.currentTimeMillis();
        if (totalArticleExpireTime < now) {
            synchronized (ReadService.class) {
                totalArticleCache = storageService.getArticleCount();
                totalArticleExpireTime = now + FIVE_MIN;
            }
        }
        return totalArticleCache;
    }

}
