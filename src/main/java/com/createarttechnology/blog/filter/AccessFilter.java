package com.createarttechnology.blog.filter;

import com.createarttechnology.logger.Logger;
import com.createarttechnology.jutil.AntiBotUtil;
import com.createarttechnology.jutil.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lixuhui on 2018/2/27.
 */
public class AccessFilter implements Filter {

    private static final Logger accessLog = Logger.getLogger("AccessLog");

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String userAgent = req.getHeader("User-Agent");
        String ip = req.getRemoteHost();
        String uri = req.getRequestURI();
        if (StringUtil.isNotEmpty(req.getQueryString())) {
            uri += "?" + req.getQueryString();
        }
        String method = req.getMethod();

        boolean isBot = AntiBotUtil.isBot(req);

        chain.doFilter(request, response);

        int retCode = resp.getStatus();
        if (!(uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".jpg") || uri.endsWith(".png")
                || uri.startsWith("/assets") || uri.startsWith("/static"))) {
            accessLog.info("{}\t{}\t{}\t{}\t{}\tua:{}",
                    isBot ? 1 : 0,
                    retCode,
                    uri,
                    method,
                    ip,
                    userAgent);
        }

    }

    public void destroy() {

    }
}
