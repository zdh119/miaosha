package com.example.miaosha.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.miaosha.mapper.MiaoshaOrderMapper;
import com.example.miaosha.mapper.OrderInfoMapper;
import com.example.miaosha.pojo.MiaoshaGoods;
import com.example.miaosha.pojo.MiaoshaOrder;
import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.pojo.OrderInfo;
import com.example.miaosha.redis.OrderKey;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.util.RedisUtil;
import com.example.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MiaoshaOrderMapper miaoshaOrderMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        MiaoshaOrder miaoshaOrder = (MiaoshaOrder) redisUtil.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId);
        if (miaoshaOrder != null){
            return miaoshaOrder;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("goods_id", goodsId);
        List<MiaoshaOrder> res = miaoshaOrderMapper.selectByMap(map);
        return res==null || res.size()==0?null:res.get(0);
    }

    /**
     * 防止同一人同时发送多次请求秒杀多次，miaosha_order中唯一索引，重复插入报错回滚
     * @param user
     * @param goodsVo
     * @return
     */
    @Override
    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo) {
        OrderInfo orderInfo = getOrderInfo(user, goodsVo);
        orderInfoMapper.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = getMiaoshaOrder(user, goodsVo, orderInfo);
        miaoshaOrderMapper.insert(miaoshaOrder);
        redisUtil.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goodsVo.getId(), miaoshaOrder);
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderById(long orderId) {
        return orderInfoMapper.selectById(orderId);
    }

    private OrderInfo getOrderInfo(MiaoshaUser user, GoodsVo goodsVo){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        return orderInfo;
    }

    private MiaoshaOrder getMiaoshaOrder(MiaoshaUser user, GoodsVo goodsVo, OrderInfo orderInfo){
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setUserId(user.getId());
        return miaoshaOrder;
    }


}
