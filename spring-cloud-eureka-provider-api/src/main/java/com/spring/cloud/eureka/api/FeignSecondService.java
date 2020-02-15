package com.spring.cloud.eureka.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "EUREKA-PROVIDER")
@RequestMapping("/feign/second")
public interface FeignSecondService {

    @RequestMapping("/test/{name}")
    public String testFeign(@PathVariable("name") String name);
}
