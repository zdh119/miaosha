package com.example.miaosha.vo;

import com.example.miaosha.pojo.MiaoshaUser;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private MiaoshaUser user;
}
