package com.createarttechnology.blog.util;

import com.createarttechnology.blog.bean.request.SaveArticleReq;
import com.createarttechnology.blog.bean.request.SaveTagReq;
import com.createarttechnology.common.BaseResp;
import com.createarttechnology.common.ErrorInfo;
import com.createarttechnology.jutil.StringUtil;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class Checker {

    public static BaseResp checkSaveArticleReq(SaveArticleReq req, boolean modify) {
        BaseResp resp = new BaseResp(ErrorInfo.INVALID_PARAMS);
        if (req == null) return resp;

        if ((modify && req.getId() <= 0) || (!modify && req.getId() > 0)) return resp;

        if (StringUtil.isEmpty(req.getTitle())) return resp.setMsg("title error");
        if (StringUtil.isEmpty(req.getContent())) return resp.setMsg("content error");
        if (StringUtil.isEmpty(req.getMarkdown())) return resp.setMsg("markdown error");

        return resp.setErrorInfo(ErrorInfo.SUCCESS);
    }

    public static BaseResp checkSaveTagReq(SaveTagReq req, boolean modify) {
        BaseResp resp = new BaseResp(ErrorInfo.INVALID_PARAMS);
        if (req == null)
            return resp;

        if ((modify && req.getId() <= 0) || (!modify && req.getId() > 0)) return resp;

        if (req.getParentId() < 0) return resp.setMsg("parent id error");
        if (StringUtil.isEmpty(req.getName())) return resp.setMsg("name error");
        if (req.getId() > 0 && req.getId() == req.getParentId()) return resp.setMsg("invalid parent id");

        return resp.setErrorInfo(ErrorInfo.SUCCESS);
    }


}
