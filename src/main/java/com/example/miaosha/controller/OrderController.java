package com.example.miaosha.controller;

import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.pojo.OrderInfo;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.result.Result;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.util.RedisUtil;
import com.example.miaosha.vo.GoodsVo;
import com.example.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(
            Model model,
            MiaoshaUser miaoshaUser,
            @RequestParam("orderId")long orderId){
        /**
         * 拦截器实现
         */
        if (miaoshaUser == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }

        long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(orderInfo);
        vo.setGoods(goodsVo);
        return Result.success(vo);
    }
}
