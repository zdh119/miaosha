package com.example.miaosha.controller;

import com.example.miaosha.pojo.MiaoshaOrder;
import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.pojo.OrderInfo;
import com.example.miaosha.rabbitmq.MQSender;
import com.example.miaosha.rabbitmq.MiaoshaMessage;
import com.example.miaosha.redis.GoodsKey;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.result.Result;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaService;
import com.example.miaosha.service.MiaoshaUserService;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.util.RedisUtil;
import com.example.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private MQSender mqSender;

    private HashMap<Long, Boolean> localOverMap = new HashMap<>();


    @RequestMapping(value="/path", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode
    ) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        String path  =miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    @PostMapping("/{path}/do_miaosha")
    @ResponseBody
    public Result<Integer> mioaosha(
            Model model,
            MiaoshaUser user,
            @RequestParam("goodsId")long goodsId,
            @PathVariable("path")String path){
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("user", user);
        //验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        boolean over = localOverMap.get(goodsId);
        if (over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        /**
         * 修改后
         */
        long stock = redisUtil.decr(GoodsKey.getMiaoshaGoodsStock, ""+goodsId, 1);
        if (stock <= 0){
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        // 判断是否秒杀到
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order !=null){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        // 入队
        MiaoshaMessage message = new MiaoshaMessage();
        message.setMiaoshaUser(user);
        message.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(message);
        return Result.success(0);

//        // 判断库存
//        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goodsVo.getStockCount();
//        if (stock<=0){
////            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
//            return Result.error(CodeMsg.MIAO_SHA_OVER);
//        }
//
//        // 判断是否已经秒杀到了
//        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if (miaoshaOrder != null){
////            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//            return Result.error(CodeMsg.REPEATE_MIAOSHA);
//        }
//        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);
////        model.addAttribute("orderInfo", orderInfo);
////        model.addAttribute("goods", goodsVo);
//        return Result.success(orderInfo);
    }

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null){
            return;
        }
        for (GoodsVo goodsVo: goodsVoList){
            // 永久不失效
            redisUtil.set(GoodsKey.getMiaoshaGoodsStock, ""+goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }

    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result  =miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @GetMapping(value = "/reset")
    @ResponseBody
    public Result<Boolean> reset(Model model){
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsVoList) {
            goods.setStockCount(10);
            redisUtil.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
//        redisUtil.del(OrderKey.getMiaoshaOrderByUidGid);
//        redisUtil.del(MiaoshaKey.isGoodsOver);
//        miaoshaService.reset(goodsList);
        return Result.success(true);
    }
}
