/*
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-09
 */
package com.spring.cloud.hystrix.feign.service;

import org.springframework.stereotype.Component;

/**
 * 断路器请求超时或失败返回信息
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-02-09
 * @since 1.0.0
 */
@Component
public class HystrixFeignServiceError implements HystrixFeignService {
    @Override
    public String feign(String name) {
        return "sorry " + name + ", system error!";
    }
}
