package com.example.miaosha.rabbitmq;

import com.example.miaosha.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;


    public void sendMiaoshaMessage(MiaoshaMessage message){
        String msg = RedisUtil.beanToString(message);
        log.info("send message:" + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, true);
    }
    public void sendMiaoshaMessage(String message){

        log.info("send message:" + message);
        amqpTemplate.convertAndSend(MQConfig.TEST_QUEUE, true);
    }
}
