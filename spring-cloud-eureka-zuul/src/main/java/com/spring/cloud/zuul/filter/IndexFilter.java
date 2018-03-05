/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-28
 */
package com.spring.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义服务过滤
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-02-28
 * @since 1.0.0
 */
@Component
public class IndexFilter extends ZuulFilter {
    /**
     * filterType返回一个字符串代表过滤器的类型，返回的字符串及表示的含义如下：
     * pre：在请求被路由之前调用
     * route：在请求被路由之时调用
     * post：在请求被路由之后被调用
     * error：处理请求发送错误时被调用
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 定义过滤器的执行顺序，数值越大优先级越低
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 判断过滤器是否要过滤，true表示过滤器总是生效(这里可以进行相应的逻辑判断过滤器是否生效)
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器具体的逻辑实现
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        String name = request.getParameter("name");

        System.out.println("[IndexFilter] >>> name = " + name);

        if ("jack".equals(name)) {
            context.setResponseStatusCode(200);
            context.setSendZuulResponse(true);//true表示对该请求进行路由，false表示过滤该请求，不进行路由
            context.set("isSuccess", true);//告诉下一个Filter上一个Filter的状态
        } else {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(1001);
            context.setResponseBody("name is error! name = " + name);//返回错误的内容
            context.set("isSuccess", false);
        }

        return null;
    }
}
