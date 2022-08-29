package com.example.miaosha.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("miaosha_user")
public class MiaoshaUser {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @TableField(value = "nickname")
    private String nickname;
    @TableField(value = "password")
    private String password;
    @TableField(value = "salt")
    private String salt;
    @TableField(value = "head")
    private String head;
    @TableField(value = "register_date")
    private Date registerDate;
    @TableField(value = "last_login_date")
    private Date lastLoginDate;
    @TableField(value = "login_count")
    private Integer loginCount;

}
