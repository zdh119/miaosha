package com.example.miaosha.service;

import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.pojo.OrderInfo;
import com.example.miaosha.vo.GoodsVo;

public interface MiaoshaService {

    OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo);

    long getMiaoshaResult(Long userId, long goodsId);

    boolean checkPath(MiaoshaUser user, long goodsId, String path);

    String createMiaoshaPath(MiaoshaUser user, long goodsId);
}
