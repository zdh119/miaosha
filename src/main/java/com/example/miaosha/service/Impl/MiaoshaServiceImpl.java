package com.example.miaosha.service.Impl;

import com.example.miaosha.pojo.MiaoshaOrder;
import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.pojo.OrderInfo;
import com.example.miaosha.redis.GoodsKey;
import com.example.miaosha.redis.MiaoshaKey;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaService;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.util.MD5Util;
import com.example.miaosha.util.RedisUtil;
import com.example.miaosha.util.UUIDUtil;
import com.example.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("miaoshaService")
public class MiaoshaServiceImpl implements MiaoshaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
        int success = goodsService.reduceStock(goodsVo);
        if(success>0) {
            //order_info maiosha_order
            return orderService.createOrder(user, goodsVo);
        }else {
            setGoodsOver(goodsVo.getId());
            return null;
        }
    }

    @Override
    public long getMiaoshaResult(Long userId, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null){
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisUtil.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisUtil.hasKey(MiaoshaKey.isGoodsOver, ""+goodsId);
    }

    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if(user == null || path == null) {
            return false;
        }
        String pathOld = (String) redisUtil.get(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ goodsId);
        return path.equals(pathOld);
    }

    public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisUtil.set(MiaoshaKey.getMiaoshaPath, ""+user.getId() + "_"+ goodsId, str);
        return str;
    }
}
