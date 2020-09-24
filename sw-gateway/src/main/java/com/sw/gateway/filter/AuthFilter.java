package com.sw.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sw.cache.util.CacheUtil;
import com.sw.common.constants.BaseConstants;
import com.sw.common.constants.SecurityConstants;
import com.sw.common.util.JwtUtil;
import com.sw.common.util.StringUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:  是否登录验证2001
 * @Author:       allenyll
 * @Date:         2020/9/3 11:57 上午
 * @Version:      1.0
 */
@Slf4j
@Component
public class AuthFilter extends ZuulFilter {

    @Value("${sw.sso.url}")
    private String login_url;

    @Value("${sw.sso.auth.url}")
    private String auth_url;

    @Value("${sw.web.url}")
    private String web_url;

    @Autowired
    CacheUtil cacheUtil;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request  = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return null;
        }
        String path = request.getRequestURI();
        StringBuffer url = request.getRequestURL();
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String loginType = request.getHeader(BaseConstants.LOGIN_TYPE);

        // 认证信息为空，跳转到登录页面
        String service = auth_url + "?target=" + web_url + path;
        String redirectUrl = login_url + "?from=" + service;
        if (StringUtil.isEmpty(header)) {
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Claims claims = null;
        try {
            claims = JwtUtil.verifyToken(header);
        } catch (Exception e) {
            log.info("获取Claims失败！");
            e.printStackTrace();
        }
        String username = (String) claims.get("username");
        String clientId = (String) claims.get("client_id");
        String jti = (String) claims.get("jti");
        // 查看缓存是否失效
        String redisToken = cacheUtil.get(jti, String.class);
        if (StringUtil.isEmpty(redisToken)) {
            try {
                ctx.setSendZuulResponse(false);
                ctx.getResponse().sendRedirect(redirectUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (StringUtil.isNotEmpty(username)) {
            ctx.addZuulRequestHeader(SecurityConstants.USER_HEADER, username);
            if (BaseConstants.SW_WECHAT.equals(loginType)) {
                ctx.addZuulRequestHeader(SecurityConstants.LOGIN_TYPE, loginType);
            } else {
                ctx.addZuulRequestHeader(SecurityConstants.LOGIN_TYPE, BaseConstants.SYSTEM_WEB);
            }
        }

        return null;
    }
}
