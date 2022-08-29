package com.example.miaosha.controller;

import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.result.Result;
import com.example.miaosha.service.MiaoshaUserService;
import com.example.miaosha.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser miaoshaUser){
        return Result.success(miaoshaUser);
    }
}
