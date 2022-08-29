package com.example.miaosha.service.Impl;

import com.example.miaosha.mapper.MiaoshaGoodsMapper;
import com.example.miaosha.pojo.MiaoshaGoods;
import com.example.miaosha.service.MiaoshaGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("miaoshaGoodsService")
public class MiaoshaGoodsServiceImpl implements MiaoshaGoodsService {

    @Autowired
    private MiaoshaGoodsMapper miaoshaGoodsMapper;

    @Override
    public List<MiaoshaGoods> listMiaoshaGoods() {
        return miaoshaGoodsMapper.selectList(null);
    }

    @Override
    public MiaoshaGoods getMiaoshaGoodsById(long goodsId) {
        return miaoshaGoodsMapper.selectById(goodsId);
    }

    @Override
    public int reduceStock(MiaoshaGoods miaoshaGoods) {
        return miaoshaGoodsMapper.reduceStock(miaoshaGoods);
    }
}
