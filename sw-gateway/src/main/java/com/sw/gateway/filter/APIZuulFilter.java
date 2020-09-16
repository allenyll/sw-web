//package com.sw.gateway.filter;
//
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Component
//public class APIZuulFilter extends ZuulFilter {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(APIZuulFilter.class);
//
//    /**
//     * 过滤器类型
//     * @return
//     */
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    /**
//     * 过滤器执行顺序，数值越小优先级越高
//     * @return
//     */
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//    /**
//     * 是否需要过滤
//     * @return
//     */
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    /**
//     * 过滤器的具体逻辑
//     * @return
//     */
//    @Override
//    public Object run() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request  = ctx.getRequest();
//
//        LOGGER.info("send {} request to {}", request.getMethod(), request.getRequestURI().toString());
//
//        String accessToken = request.getParameter("accessToken");
//
////        if ("".equals(accessToken) || accessToken == null) {
////            LOGGER.warn("access token is empty");
////            ctx.setSendZuulResponse(false);
////            ctx.setResponseStatusCode(401);
////            return null;
////        }
//
//        LOGGER.info("access token ok");
//        return null;
//    }
//}
