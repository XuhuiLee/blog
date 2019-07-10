package com.createarttechnology.blog.service;

import com.alibaba.fastjson.JSON;
import com.createarttechnology.blog.bean.request.SaveArticleReq;
import com.createarttechnology.blog.bean.request.SaveTagReq;
import com.createarttechnology.blog.dao.entity.ArticleEntity;
import com.createarttechnology.blog.dao.entity.TagEntity;
import com.createarttechnology.blog.util.RichTextUtil;
import com.createarttechnology.common.BaseResp;
import com.createarttechnology.common.ErrorInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lixuhui on 2018/9/14.
 */
@Service
public class WriteService {

    @Resource
    private StorageService storageService;
    @Resource
    private TagService tagService;


    public BaseResp saveArticle(SaveArticleReq req, boolean modify) {
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(req.getTitle());
        entity.setRichContent(req.getContent());
        entity.setSimpleContent(RichTextUtil.toSimpleText(req.getContent()));
        entity.setTag(req.getTag());
        entity.setMarkdown(req.getMarkdown());
        List<String> pics = RichTextUtil.extractPicsFromContentByJSoup(entity.getRichContent());
        if (CollectionUtils.isNotEmpty(pics)) {
            entity.setPics(JSON.toJSONString(pics));
        }

        BaseResp<Long> resp = new BaseResp<>();
        if (!modify) {
            long id = storageService.saveArticleEntity(entity);
            if (id > 0) {
                resp.setErrorInfo(ErrorInfo.SUCCESS).setData(id);
            }
            return resp;
        } else {
            storageService.updateArticleEntity(req.getId(), entity);
            return resp.setErrorInfo(ErrorInfo.SUCCESS).setData(req.getId());
        }
    }

    public BaseResp saveTag(SaveTagReq req, boolean modify) {
        BaseResp<Integer> resp = new BaseResp<>();
        if (!TagService.isValidParentId(req.getParentId())) {
            return resp.setErrorInfo(ErrorInfo.INVALID_PARAMS);
        }

        TagEntity entity = new TagEntity();
        entity.setParentId(req.getParentId());
        entity.setName(req.getName());

        if (!modify) {
            int id = storageService.saveTagEntity(entity);
            if (id > 0) {
                resp.setErrorInfo(ErrorInfo.SUCCESS).setData(id);
            }
        } else {
            storageService.updateTagEntity(req.getId(), entity);
            resp.setErrorInfo(ErrorInfo.SUCCESS).setData(req.getId());
        }
        tagService.reloadTagCache();
        return resp;
    }

}
