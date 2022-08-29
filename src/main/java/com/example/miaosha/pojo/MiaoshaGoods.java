package com.example.miaosha.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("miaosha_goods")
public class MiaoshaGoods {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    @TableField(value = "goods_id")
    private Long goodsId;
    @TableField(value = "miaosha_price")
    private Double miaohsaPrice;
    @TableField(value = "stock_count")
    private Integer stockCount;
    @TableField(value = "start_date")
    private Date startDate;
    @TableField(value = "end_date")
    private Date endDate;
}
