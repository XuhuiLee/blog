package com.createarttechnology.blog.service;

import com.createarttechnology.common.BaseResp;
import com.createarttechnology.common.ErrorInfo;
import com.createarttechnology.constant.StringConstant;
import com.createarttechnology.jutil.RandomUtil;
import com.createarttechnology.logger.Logger;
import com.createarttechnology.supporter.redis.LoginClient;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录相关服务
 * Created by lixuhui on 2019/7/23.
 */
@Service
public class LoginService {

    private static final Logger logger = Logger.getLogger(LoginService.class);

    private static final int EXPIRE_TIME = 30 * 24 * 60 * 60;

    @Resource
    private ConfigService configService;

    public BaseResp login(HttpServletResponse response, String username, String password) {
        /*
        登录设计：
        1. 验证用户名密码
        2. 生成随机字符串令牌
        3. uid和令牌保存到redis及cookie，过期时间30天
        4. 每次验证时检查cookie中令牌、uid与redis中对应

        好处：
        1. 屏蔽校验信息
        2. 单端登录
        3. 即使知道登录策略，破解难度也很大
        4. 即使拿到uid和随机字符串令牌，也会在用户登录或超时后失效
        5. 拿到令牌无法反推用户名密码

        坏处：
        1. 考虑到源码上传github，那么就假定令牌生成策略已经被人知道，因此不能使用特定的令牌生成策略，否则可伪造令牌
        2. 使用随机策略，就仅能支持单端登录
        3. 避免暴力破解，需要增加接口频率限制
         */
        BaseResp resp = new BaseResp();
        logger.info("password={}, md5={}", password, DigestUtils.md5DigestAsHex("password".getBytes()));
        if (configService.getUsername().equals(username) && DigestUtils.md5DigestAsHex(configService.getPassword().getBytes()).equalsIgnoreCase(password)) {
            String token = RandomUtil.randomString(32);
            // admin使用uid=1，保存到redis
            LoginClient.setLogin(1, token);

            Cookie lk = new Cookie(StringConstant.COOKIE_LOGIN_KEY, "1");
            Cookie ltk = new Cookie(StringConstant.COOKIE_LOGIN_TOKEN, token);
            lk.setMaxAge(EXPIRE_TIME);
            ltk.setMaxAge(EXPIRE_TIME);
            response.addCookie(lk);
            response.addCookie(ltk);
            return resp.setErrorInfo(ErrorInfo.SUCCESS);
        } else {
            delLoginCookies(response);
            return resp.setErrorInfo(ErrorInfo.NO_AUTH);
        }
    }

    public BaseResp logout(HttpServletResponse response) {
        BaseResp resp = new BaseResp();
        // redis内容不用管，下次登录会更新redis，只清除cookie就行
        delLoginCookies(response);
        return resp.setErrorInfo(ErrorInfo.SUCCESS);
    }

    private void delLoginCookies(HttpServletResponse response) {
        Cookie lk = new Cookie(StringConstant.COOKIE_LOGIN_KEY, "");
        Cookie ltk = new Cookie(StringConstant.COOKIE_LOGIN_TOKEN, "");
        lk.setMaxAge(0);
        ltk.setMaxAge(0);
        response.addCookie(lk);
        response.addCookie(ltk);
    }

}
