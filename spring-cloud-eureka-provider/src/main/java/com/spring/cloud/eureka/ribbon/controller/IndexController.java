/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-06
 */
package com.spring.cloud.eureka.ribbon.controller;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/ribbon")
    public String ribbon(String name) {
        return "hello "  + name + ", welcome to ribbon world!";
    }

    @RequestMapping(value = "/feign", method = RequestMethod.GET)
    public String feign(String name) {
        return "hello "  + name + ", welcome to feign world!";
    }

}
