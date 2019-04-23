package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.util.Converter;
import com.createarttechnology.logger.Logger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * Created by lixuhui on 2018/9/18.
 */
@Service
public class TagService {

    private static final Logger logger = Logger.getLogger(TagService.class);

    // 最多支持三层tag
    // 1级（顶级）tagId
    private static Set<Integer> L1_TAG_ID_CACHE = Sets.newHashSet();
    // 2级tagId
    private static Set<Integer> L2_TAG_ID_CACHE = Sets.newHashSet();

    // 包含顶级tag
    private static List<Tag> TOP_TAG_CACHE = Lists.newArrayList();
    // 包含全部tag
    private static Map<Integer, Tag> TAG_CACHE = Maps.newLinkedHashMap();
    // tag全路径
    private static final Map<Integer, List<Tag>> TAG_PATH_CACHE = Maps.newConcurrentMap();
    // tag id全路径
    private final Map<Integer, Set<Integer>> tagIdPathCache = Maps.newConcurrentMap();


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
        Set<Integer> l1TagIdCache = Sets.newHashSet();
        Set<Integer> l2TagIdCache = Sets.newHashSet();
        List<Tag> topTagCache = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(tagList)) {
            for (Tag tag : tagList) {
                // tag全部装入
                tagCache.put(tag.getId(), tag);
                // 无父节点的tag，保存到顶级tag缓存中
                if (tag.getParentId() == 0) {
                    tag.setLevel(1);
                    l1TagIdCache.add(tag.getId());
                    topTagCache.add(tag);
                }
            }
            for (Tag tag : tagList) {
                // 父节点在顶级tag缓存中的，保存到二级tag缓存中
                if (l1TagIdCache.contains(tag.getParentId())) {
                    tag.setLevel(2);
                    l2TagIdCache.add(tag.getId());
                    tagCache.get(tag.getParentId()).addSubTag(tag);
                }
            }
            for (Tag tag : tagList) {
                // 同上保存三级tag缓存
                if (l2TagIdCache.contains(tag.getParentId())) {
                    tag.setLevel(3);
                    tagCache.get(tag.getParentId()).addSubTag(tag);
                }
            }
        }

        TAG_CACHE = tagCache;
        L1_TAG_ID_CACHE = l1TagIdCache;
        L2_TAG_ID_CACHE = l2TagIdCache;
        TOP_TAG_CACHE = topTagCache;
        // 清理全路径重新装载
        TAG_PATH_CACHE.clear();
        tagIdPathCache.clear();
    }

    /**
     * 从缓存读tag
     */
    public Tag getTag(int id) {
        return TAG_CACHE.get(id);
    }

    /**
     * 根据tagId获取父路径，列表页详情页等展示tag时用
     */
    public List<Tag> getParentTagPath(int id) {
        // 首先从缓存读
        if (TAG_PATH_CACHE.containsKey(id)) {
            return TAG_PATH_CACHE.get(id);
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
        TAG_PATH_CACHE.put(id, pathList);
        return pathList;
    }

    /**
     * 根据tagId获取子路径，列表页查询tag下数据时用
     */
    public Set<Integer> getChildTagIdPath(int id) {
        // 首先从缓存读
        if (tagIdPathCache.containsKey(id)) {
            return tagIdPathCache.get(id);
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
        tagIdPathCache.put(id, ids);
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
    public boolean isValidParentId(int parentId) {
        return parentId == 0 || L1_TAG_ID_CACHE.contains(parentId) || L2_TAG_ID_CACHE.contains(parentId);
    }

}
