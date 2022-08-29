package com.example.miaosha;

import com.example.miaosha.mapper.MiaoshaUserMapper;
import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.util.MD5Util;
import com.example.miaosha.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@SpringBootTest
class MiaoshaApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MiaoshaUserMapper miaoshaUserMapper;


    @Test
    void contextLoads() {
        System.out.println(redisUtil.set("test", "nihao"));
//        System.out.println((String)redisUtil.get("test"));
    }

    @Test
    void testDB(){
        String mobile = "13000000000";
        Long id = Long.parseLong(mobile);
        MiaoshaUser user = miaoshaUserMapper.selectById(id);
        System.out.println(user);
    }

    @Test
    void getPass(){
        String dbpass = "68156ba662e422b83001bdc017eb83bc";
        String test1 = MD5Util.inputPassToFormPass("123456");
        String test2 = MD5Util.formPassToDBPass(test1, "1a2b3c4d");
        String target = MD5Util.inputPassToDBPass("123456", "1a2b3c4d");
        System.out.println(test1);
        System.out.println(test2);
        System.out.println(target);
    }

}
