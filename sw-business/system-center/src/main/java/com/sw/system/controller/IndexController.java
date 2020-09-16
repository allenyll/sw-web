package com.sw.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 首页控制器
 * @Author: yu.leilei
 * @Date: 上午 11:28 2018/6/12 0012
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String index(){
        return "index";
    }

}
