package com.example.miaosha.rabbitmq;

import com.example.miaosha.pojo.MiaoshaOrder;
import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.service.GoodsService;
import com.example.miaosha.service.MiaoshaService;
import com.example.miaosha.service.OrderService;
import com.example.miaosha.util.RedisUtil;
import com.example.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message) {
        log.info("receive message:"+message);
        MiaoshaMessage miaoshaMessage = RedisUtil.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = miaoshaMessage.getMiaoshaUser();
        long goodsId = miaoshaMessage.getGoodsId();

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        if (goodsVo==null || goodsVo.getStockCount()<=0){
            return;
        }
        // 判断是否已经秒杀，避免重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId() , goodsId);
        if (order==null){
            return;
        }
        // 减缓存，下订单，写入秒杀订单
        miaoshaService.miaosha(user, goodsVo);
    }
    @RabbitListener(queues = MQConfig.TEST_QUEUE)
    public void testReceive(String message) {
        log.info("receive message:"+message);
    }
}
