/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-06
 */
package com.spring.cloud.eureka.ribbon.controller;

import com.spring.cloud.eureka.ribbon.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-02-06
 * @since 1.0.0
 */
@RestController
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(String name) {
        return indexService.hello(name);
    }

    @RequestMapping(value = "/ribbon", method = RequestMethod.GET)
    public String configurationRibbonByCode(String name) {
        ServiceInstance instance = loadBalancerClient.choose("eureka-provider");
        System.out.println("http://" + instance.getHost() + ":" + instance.getPort());
        return indexService.hello(name);
    }
}
