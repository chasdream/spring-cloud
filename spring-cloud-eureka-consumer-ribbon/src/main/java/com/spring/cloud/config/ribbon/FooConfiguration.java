/* 
 * Copyright (C), 2017-2018
 * File Name: @(#)chasdream
 * Encoding UTF-8
 * Author: chasdream
 * Version: 1.0.0
 * Date: 2018-02-07
 */
package com.spring.cloud.config.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ribbon配置类
 * <p>
 * <p>
 * <p>
 * <a href="chasdream"><i>View Source</i></a>
 *
 * @author chasdream
 * @version 1.0.0
 * @date 2018-02-07
 * @since 1.0.0
 */
//@Configuration
//public class FooConfiguration {
//
//    @Bean
//    public IRule ribbonRule(IClientConfig config) {
////        return new BestAvailableRule(); //最小的并发请求的server
////        return new WeightedResponseTimeRule(); //加权响应时间负载均衡
////        return new RetryRule(); //在选定的负载均衡策略机上重试机制
////        return new RoundRobinRule(); //roundRobin方式轮询
////        return new ZoneAvoidanceRule(); //区域感知轮询负载均衡
//        return new RandomRule();//随机加载
//    }
//}
