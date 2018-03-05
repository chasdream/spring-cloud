/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-03-01
 */
package com.spring.cloud.config.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 功能描述
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-03-01
 * @since 1.0.0
 */
@Controller
public class ConfigController {

    @Value("${name}")
    public String name;

    @ResponseBody
    @RequestMapping(value = "/config")
    public String config() {
        return name;
    }
}
