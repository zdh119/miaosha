package com.example.miaosha.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.miaosha.mapper.GoodsMapper;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    @Override
    public int reduceStock(GoodsVo goods) {
        UpdateWrapper<GoodsVo> wrapper = new UpdateWrapper<>();
        /**
         * 防止同时进入当无库存减为负数
         */
        wrapper.gt("stock_count", 0);
        return goodsMapper.reduceStock(goods, wrapper);
    }
}
