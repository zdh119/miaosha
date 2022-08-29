package com.example.miaosha.service;

import com.example.miaosha.vo.GoodsVo;

import java.util.List;

public interface GoodsService {

    List<GoodsVo> listGoodsVo();

    GoodsVo getGoodsVoByGoodsId(long goodsId);

    int reduceStock(GoodsVo goods);
}
