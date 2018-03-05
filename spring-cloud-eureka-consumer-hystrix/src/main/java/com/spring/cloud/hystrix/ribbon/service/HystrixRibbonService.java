/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-08
 */
package com.spring.cloud.hystrix.ribbon.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * hystrix断路器实现
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-02-08
 * @since 1.0.0
 */
@Service
public class HystrixRibbonService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "fallback")
    public String index(String name) {
        return restTemplate.getForObject("http://EUREKA-PROVIDER/ribbon?name=" + name, String.class);
    }

    public String fallback(String name) {
        return "sorry, " + name + " system request error! ribbon";
    }
}
