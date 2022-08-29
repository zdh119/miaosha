package com.example.miaosha.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.miaosha.pojo.MiaoshaGoods;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface MiaoshaGoodsMapper extends BaseMapper<MiaoshaGoods> {

    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId")
    int reduceStock(MiaoshaGoods g);

}
