package com.example.miaosha.controller;

import com.example.miaosha.rabbitmq.MQSender;
import com.example.miaosha.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")

public class DemoController {

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("name", "demo");
        return "hello";
    }

    @RequestMapping("/redis")
    @ResponseBody
    public String redisGet(){

        return "";
    }

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.sendMiaoshaMessage("hello mq");
        return Result.success("hello, world");
    }

}
