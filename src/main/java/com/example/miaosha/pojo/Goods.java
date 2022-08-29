package com.example.miaosha.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("goods")
public class Goods {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    @TableField(value = "goods_name")
    private String goodsName;
    @TableField(value = "goods_title")
    private String goodsTitle;
    @TableField(value = "goods_img")
    private String goodsImg;
    @TableField(value = "goods_detail")
    private String goodsDetail;
    @TableField(value = "goods_price")
    private Double goodsPrice;
    @TableField(value = "goods_stock")
    private Integer goodsStock;
}
