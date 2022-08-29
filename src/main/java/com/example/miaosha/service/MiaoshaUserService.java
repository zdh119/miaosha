package com.example.miaosha.service;

import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

public interface MiaoshaUserService {

    MiaoshaUser getById(long id);

    boolean updatePassword(String token, long id, String formPass);

    MiaoshaUser getByToken(HttpServletResponse response, String token);

    boolean login(HttpServletResponse response, LoginVo loginVo);
}
