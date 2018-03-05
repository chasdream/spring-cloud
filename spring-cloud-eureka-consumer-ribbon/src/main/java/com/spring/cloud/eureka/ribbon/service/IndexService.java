/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-06
 */
package com.spring.cloud.eureka.ribbon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
@Service
public class IndexService {

    private static final String PATH = "http://EUREKA-PROVIDER/ribbon";

    @Autowired
    private RestTemplate restTemplate;

    public String hello(String name) {
        return restTemplate.getForObject(PATH + "?name=" + name, String.class);
    }
}
