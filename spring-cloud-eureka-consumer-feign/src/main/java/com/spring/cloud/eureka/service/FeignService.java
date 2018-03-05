/* 
 * Copyright (C), 2015-2016
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-07
 */
package com.spring.cloud.eureka.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * feign调用接口服务
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-02-07
 * @since 1.0.0
 */
@FeignClient(value = "EUREKA-PROVIDER")
public interface FeignService {

    @RequestMapping(value = "/feign", method = RequestMethod.GET)
    String feign(@RequestParam(value = "name") String name);
}
