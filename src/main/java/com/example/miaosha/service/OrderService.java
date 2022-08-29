package com.example.miaosha.service;

import com.example.miaosha.pojo.MiaoshaGoods;
import com.example.miaosha.pojo.MiaoshaOrder;
import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.pojo.OrderInfo;
import com.example.miaosha.vo.GoodsVo;

public interface OrderService {

    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);

    OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo);

    OrderInfo getOrderById(long orderId);



}
