package com.sw.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gateway")
public class TestController {

    @RequestMapping("test")
    public void hello() {
        System.out.println("hello");
    }
}
