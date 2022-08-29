package com.example.miaosha.service.Impl;

import com.example.miaosha.exception.GlobalException;
import com.example.miaosha.mapper.MiaoshaUserMapper;
import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.redis.MiaoshaUserKey;
import com.example.miaosha.result.CodeMsg;
import com.example.miaosha.service.MiaoshaUserService;
import com.example.miaosha.util.MD5Util;
import com.example.miaosha.util.RedisUtil;
import com.example.miaosha.util.UUIDUtil;
import com.example.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service("miaoshaUserService")
public class MiaoshaUserServiceImpl implements MiaoshaUserService {
    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserMapper miaoshaUserMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public MiaoshaUser getById(long id) {
        /**
         * 取缓存
         * */
        MiaoshaUser user = (MiaoshaUser) redisUtil.get(MiaoshaUserKey.getById, ""+id);
        if (user != null){
            return user;
        }
        user = miaoshaUserMapper.selectById(id);
        if (user != null){
            redisUtil.set(MiaoshaUserKey.getById, ""+id, user); // 永久？
        }
        return user;
    }

    @Override
    public boolean updatePassword(String token, long id, String formPass) {
        // 取user
        MiaoshaUser user = getById(id);
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // 更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserMapper.updateById(toBeUpdate);
        // 处理缓存
        redisUtil.del(MiaoshaUserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisUtil.set(MiaoshaUserKey.token, token, user);
        return false;
    }

    @Override
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser miaoshaUser = (MiaoshaUser) redisUtil.get(MiaoshaUserKey.token, token);
        // 延长有效期
        if (miaoshaUser!=null){
            addCookie(response, token, miaoshaUser);
        }
        return miaoshaUser;
    }

    @Override
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);

        if(!calcPass.equals(dbPass)) {
            /**
             * 待处理
             */
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisUtil.set(MiaoshaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(6000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
