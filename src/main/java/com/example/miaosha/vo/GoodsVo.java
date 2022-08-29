package com.example.miaosha.vo;

import com.example.miaosha.pojo.Goods;
import lombok.Data;

import java.util.Date;

@Data
public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
