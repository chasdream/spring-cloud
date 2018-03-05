/*
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-09
 */
package com.spring.cloud.hystrix.feign.controller;

import com.spring.cloud.hystrix.feign.service.HystrixFeignService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @date 2018-02-09
 * @since 1.0.0
 */
@RestController
public class HystrixFeignController {

    @Autowired
    private HystrixFeignService hystrixFeignService;

    @RequestMapping(value = "/feign", method = RequestMethod.GET)
    public String feignIndex(String name) {
        return hystrixFeignService.feign(name);
    }
}
