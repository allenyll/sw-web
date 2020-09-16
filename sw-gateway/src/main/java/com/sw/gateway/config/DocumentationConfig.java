package com.sw.gateway.config;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:  SWAGGER 路由配置
 * @Author:       allenyll
 * @Date:         2020/8/31 7:10 下午
 * @Version:      1.0
 */
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;

    public DocumentationConfig(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
        routes.forEach(route
                -> resources.add(swaggerResource(route.getId(),
                route.getFullPath().replace("**", "v2/api-docs"), "2.0")));

        return resources;
    }

//    @Override
//    public List<SwaggerResource> get() {
//        List resources = new ArrayList<>();
//        resources.add(swaggerResource("sw-eureka-client", "/api-sec/cloud/client/v2/api-docs", "1.0"));
//        return resources;
//    }

    private SwaggerResource swaggerResource(String routeId, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(routeId);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
