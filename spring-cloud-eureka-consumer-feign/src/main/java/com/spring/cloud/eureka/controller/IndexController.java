/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-07
 */
package com.spring.cloud.eureka.controller;

import com.spring.cloud.eureka.service.FeignService;
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
 * @date 2018-02-07
 * @since 1.0.0
 */
@RestController
public class IndexController {

    @Autowired
    private FeignService feignService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String feign(String name) {
        return feignService.feign(name);
    }
}
