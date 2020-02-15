package com.spring.cloud.eureka.service;

import com.spring.cloud.eureka.api.FeignSecondService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignSecondServiceImpl implements FeignSecondService {

    @Override
    public String testFeign(@PathVariable("name") String name) {
        return "hello, welcome to " + name;
    }
}
