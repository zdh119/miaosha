package com.example.miaosha.vo;

import com.example.miaosha.pojo.OrderInfo;
import lombok.Data;

@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
