package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.util.Converter;
import com.createarttechnology.logger.Logger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * tag服务
 * Created by lixuhui on 2018/9/18.
 */
@Service
@Lazy(false)
public class TagService {

    private static final Logger logger = Logger.getLogger(TagService.class);

    // 包含顶级tag
    private static List<Tag> TOP_TAG_CACHE = Lists.newArrayList();
    // 包含全部tag
    private static Map<Integer, Tag> TAG_CACHE = Maps.newLinkedHashMap();
    // 父全路径
    private static final Map<Integer, List<Tag>> PARENT_TAG_PATH_CACHE = Maps.newConcurrentMap();
    // 子全路径
    private static final Map<Integer, Set<Integer>> CHILD_TAG_ID_PATH_CACHE = Maps.newConcurrentMap();

    @Resource
    private StorageService storageService;

    @PostConstruct
    public void init() {
        reloadTagCache();
    }

    /**
     * 构建tag树缓存
     * 只有初始化和修改tag时才需要重建
     */
    public void reloadTagCache() {
        List<Tag> tagList = Converter.tagEntityList2TagList(storageService.getAllTagEntityList());

        Map<Integer, Tag> tagCache = Maps.newHashMap();
        List<Tag> topTagCache = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(tagList)) {
            for (Tag tag : tagList) {
                // tag全部装入
                tagCache.put(tag.getId(), tag);
                // 无父节点的tag，保存到顶级tag缓存中
                if (tag.getParentId() == 0) {
                    tag.setLevel(1);
                    topTagCache.add(tag);
                }
            }
            for (Tag tag : tagList) {
                // 父节点是一级的，保存到二级tag缓存中
                Tag parentTag = tagCache.get(tag.getParentId());
                if (parentTag != null && parentTag.getLevel() == 1) {
                    tag.setLevel(2);
                    parentTag.addSubTag(tag);
                }
            }
            for (Tag tag : tagList) {
                // 同上保存三级tag缓存
                Tag parentTag = tagCache.get(tag.getParentId());
                if (parentTag != null && parentTag.getLevel() == 2) {
                    tag.setLevel(3);
                    parentTag.addSubTag(tag);
                }
            }
        }

        TAG_CACHE = tagCache;
        TOP_TAG_CACHE = topTagCache;
        // 清理全路径重新装载
        PARENT_TAG_PATH_CACHE.clear();
        CHILD_TAG_ID_PATH_CACHE.clear();
    }

    /**
     * 从缓存读tag
     */
    public static Tag getTag(int id) {
        return TAG_CACHE.get(id);
    }

    /**
     * 根据tagId获取父路径，列表页详情页等展示tag时用
     */
    public static List<Tag> getParentTagPath(int id) {
        // 首先从缓存读
        if (PARENT_TAG_PATH_CACHE.containsKey(id)) {
            return PARENT_TAG_PATH_CACHE.get(id);
        }
        // 构建一条路径
        List<Tag> pathList = Lists.newArrayList();
        int currentId = id;
        Tag currentTag;
        // 通过parentId查找
        while ((currentTag = getTag(currentId)) != null) {
            pathList.add(currentTag);
            if (currentTag.getParentId() > 0) {
                currentId = currentTag.getParentId();
            } else {
                break;
            }
        }
        // 排序
        pathList.sort(Comparator.comparingInt(Tag::getLevel));
        // 放入缓存
        PARENT_TAG_PATH_CACHE.put(id, pathList);
        return pathList;
    }

    /**
     * 根据tagId获取子路径，列表页查询tag下数据时用
     */
    public static Set<Integer> getChildTagIdPath(int id) {
        // 首先从缓存读
        if (CHILD_TAG_ID_PATH_CACHE.containsKey(id)) {
            return CHILD_TAG_ID_PATH_CACHE.get(id);
        }
        Tag inputTag = getTag(id);
        if (inputTag == null) {
            return null;
        }
        List<Tag> tagPath = Lists.newArrayList(inputTag);
        Set<Integer> ids = Sets.newLinkedHashSet();
        while (CollectionUtils.isNotEmpty(tagPath)) {
            Tag tag = tagPath.remove(0);
            ids.add(tag.getId());
            if (CollectionUtils.isNotEmpty(tag.getSubTags())) {
                for (Tag child : tag.getSubTags()) {
                    tagPath.add(child);
                    ids.add(child.getId());
                }
            }
        }
        // 放入缓存
        CHILD_TAG_ID_PATH_CACHE.put(id, ids);
        return ids;
    }

    /**
     * 取所有顶级tag
     */
    public static List<Tag> getTopTagList() {
        return TOP_TAG_CACHE;
    }

    /**
     * 确认父节点是否合法
     */
    public static boolean isValidParentId(int parentId) {
        return parentId == 0 || (TAG_CACHE.containsKey(parentId) && TAG_CACHE.get(parentId).getLevel() < 3);
    }

}
