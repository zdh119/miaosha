package com.example.miaosha.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("miaosha_order")
public class MiaoshaOrder {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    @TableField(value = "user_id")
    private Long userId;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "goods_id")
    private Long goodsId;

}
