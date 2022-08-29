package com.example.miaosha.rabbitmq;

import com.example.miaosha.pojo.MiaoshaUser;
import lombok.Data;

@Data
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private long goodsId;
}
