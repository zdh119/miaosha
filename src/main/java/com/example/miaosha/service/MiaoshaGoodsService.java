package com.example.miaosha.service;

import com.example.miaosha.pojo.MiaoshaGoods;

import java.util.List;

public interface MiaoshaGoodsService {

    List<MiaoshaGoods> listMiaoshaGoods();

    MiaoshaGoods getMiaoshaGoodsById(long goodsId);

    int reduceStock(MiaoshaGoods miaoshaGoods);
}
