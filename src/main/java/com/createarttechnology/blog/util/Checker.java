package com.createarttechnology.blog.util;

import com.createarttechnology.blog.bean.request.SaveArticleReq;
import com.createarttechnology.blog.bean.request.SaveTagReq;
import com.createarttechnology.blog.bean.response.BaseResp;
import com.createarttechnology.blog.constants.ErrorInfo;
import com.createarttechnology.jutil.StringUtil;

/**
 * Created by lixuhui on 2018/9/14.
 */
public class Checker {

    public static BaseResp checkSaveArticleReq(SaveArticleReq req) {
        BaseResp resp = new BaseResp(ErrorInfo.INVALID_PARAMS);
        if (req == null)
            return resp;

        if (StringUtil.isEmpty(req.getTitle()))
            return resp.setMsg("title error");
        if (StringUtil.isEmpty(req.getContent()))
            return resp.setMsg("content error");

        return resp.setErrorInfo(ErrorInfo.SUCCESS);
    }

    public static BaseResp checkSaveTagReq(SaveTagReq req) {
        BaseResp resp = new BaseResp(ErrorInfo.INVALID_PARAMS);
        if (req == null)
            return resp;

        if (req.getParentId() < 0)
            return resp.setMsg("parent id error");
        if (StringUtil.isEmpty(req.getName()))
            return resp.setMsg("name error");

        return resp.setErrorInfo(ErrorInfo.SUCCESS);
    }


}
