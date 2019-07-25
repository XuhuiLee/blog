package com.createarttechnology.blog.service;

import com.createarttechnology.constant.RedisKeys;
import com.createarttechnology.jutil.CollectionUtil;
import com.createarttechnology.jutil.StringUtil;
import com.createarttechnology.logger.Logger;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import redis.clients.jedis.commands.JedisCommands;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 集中处理redis调用
 * Created by lixuhui on 2019/7/23.
 */
@Service
public class RedisService {

    private static final Logger logger = Logger.getLogger(RedisService.class);

    @Resource
    private JedisCommands blogRedis;

    public void incrPv(long articleId) {
        blogRedis.hincrBy(RedisKeys.REDIS_ARTICLE_PV_HASH, String.valueOf(articleId), 1);
    }

    public int getPv(long articleId) {
        String pv = blogRedis.hget(RedisKeys.REDIS_ARTICLE_PV_HASH, String.valueOf(articleId));
        return StringUtil.convertInt(pv, 0);
    }

    public Map<Long, Integer> batchGetPv(List<Long> articleIds) {
        if (articleIds == null) {
            return Collections.emptyMap();
        }

        String[] keys = new String[articleIds.size()];
        int i = 0;
        for (long id : articleIds) {
            keys[i++] = String.valueOf(id);
        }

        List<String> result = blogRedis.hmget(RedisKeys.REDIS_ARTICLE_PV_HASH, keys);
        if (result == null) {
            return Collections.emptyMap();
        }

        Map<Long, Integer> map = Maps.newHashMap();
        for (i = 0; i < articleIds.size(); i++) {
            map.put(articleIds.get(i), StringUtil.convertInt(result.get(i), 0));
        }

        return map;
    }

    /**
     * 目前内容量少，可以直接mgetAll
     * 后续考虑分为历史 + 当日计算
     * 暂时先不展示这块
     */
    public int getTotalPv() {
        Map<String, String> result = blogRedis.hgetAll(RedisKeys.REDIS_ARTICLE_PV_HASH);
        if (CollectionUtil.isEmpty(result)) {
            return 0;
        }

        int total = 0;
        for (String pv : result.values()) {
            total += StringUtil.convertInt(pv, 0);
        }

        return total;
    }

}
