/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-08
 */
package com.spring.cloud.hystrix.ribbon.controller;

import com.spring.cloud.hystrix.ribbon.service.HystrixRibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ribbon使用hystrix
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-02-08
 * @since 1.0.0
 */
@RestController
public class HystrixRibbonController {

    @Autowired
    private HystrixRibbonService service;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping(value = "/index")
    public String index(String name) {
        String str = service.index(name);
//        ServiceInstance instance = loadBalancerClient.choose("EUREKA-PROVIDER");
        return str/* + "[port=" + instance.getPort() + "]"*/;
    }
}
