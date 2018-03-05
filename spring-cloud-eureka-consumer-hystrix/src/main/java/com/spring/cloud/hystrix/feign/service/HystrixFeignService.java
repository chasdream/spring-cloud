/*
 * Copyright (C), 2015-2016
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-09
 */
package com.spring.cloud.hystrix.feign.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * feign集成断路器
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-02-09
 * @since 1.0.0
 */
@FeignClient(value = "EUREKA-PROVIDER", fallback = HystrixFeignServiceError.class)
public interface HystrixFeignService {

    @RequestMapping(value = "/feign", method = RequestMethod.GET)
    String feign(@RequestParam(value = "name") String name);
}
