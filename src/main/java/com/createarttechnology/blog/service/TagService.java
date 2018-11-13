package com.createarttechnology.blog.service;

import com.createarttechnology.blog.bean.response.Tag;
import com.createarttechnology.blog.util.CollectionUtil;
import com.createarttechnology.blog.util.Converter;
import com.createarttechnology.logger.Logger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by lixuhui on 2018/9/18.
 */
@Service
public class TagService {

    private static final Logger logger = Logger.getLogger(TagService.class);

    // 最多支持三层tag
    private static final List<Integer> LEVEL_1_ID_CACHE = Lists.newArrayList();
    private static final List<Integer> LEVEL_2_ID_CACHE = Lists.newArrayList();

    private static final List<Tag> TOP_TAG_CACHE = Lists.newArrayList();
    private static final Map<Integer, Tag> TAG_CACHE = Maps.newLinkedHashMap();
    private static final Map<Integer, List<Tag>> TAG_PATH_CACHE = Maps.newHashMap();


    @Resource
    private StorageService storageService;

    @PostConstruct
    public void init() {
        buildTagTree();
    }

    public void buildTagTree() {
        clearCache();

        List<Tag> tagList = Converter.tagEntityList2TagList(storageService.getAllTagEntityList());

        if (CollectionUtil.isNotEmpty(tagList)) {
            for (Tag tag : tagList) {
                TAG_CACHE.put(tag.getId(), tag);
                if (tag.getParentId() == 0) {
                    tag.setLevel(1);
                    LEVEL_1_ID_CACHE.add(tag.getId());
                    TOP_TAG_CACHE.add(tag);
                }
            }
            for (Tag tag : tagList) {
                if (LEVEL_1_ID_CACHE.contains(tag.getParentId())) {
                    tag.setLevel(2);
                    LEVEL_2_ID_CACHE.add(tag.getId());
                    TAG_CACHE.get(tag.getParentId()).addSubTag(tag);
                }
            }
            for (Tag tag : tagList) {
                if (LEVEL_2_ID_CACHE.contains(tag.getParentId())) {
                    tag.setLevel(3);
                    TAG_CACHE.get(tag.getParentId()).addSubTag(tag);
                }
            }
        }
    }

    private void clearCache() {
        LEVEL_1_ID_CACHE.clear();
        LEVEL_2_ID_CACHE.clear();
        TOP_TAG_CACHE.clear();
        TAG_CACHE.clear();
        TAG_PATH_CACHE.clear();
    }

    public Tag getTag(int id) {
        return TAG_CACHE.get(id);
    }

    public List<Tag> getTagParentPath(int id) {
        if (TAG_PATH_CACHE.get(id) != null) {
            return TAG_PATH_CACHE.get(id);
        }
        List<Tag> pathList = Lists.newArrayList();
        int currentId = id;
        Tag currentTag;
        while ((currentTag = getTag(currentId)) != null) {
            pathList.add(currentTag);
            if (currentTag.getParentId() > 0) {
                currentId = currentTag.getParentId();
            } else {
                break;
            }
        }
        pathList.sort(Comparator.comparingInt(Tag::getLevel));
        TAG_PATH_CACHE.put(id, pathList);
        return pathList;
    }

    public static List<Tag> getTopTagList() {
        return TOP_TAG_CACHE;
    }

    public boolean isValidParentId(int parentId) {
        return parentId == 0 || LEVEL_1_ID_CACHE.contains(parentId) || LEVEL_2_ID_CACHE.contains(parentId);
    }

}
