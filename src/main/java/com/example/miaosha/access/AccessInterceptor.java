package com.example.miaosha.access;

import com.example.miaosha.pojo.MiaoshaUser;
import com.example.miaosha.service.Impl.MiaoshaUserServiceImpl;
import com.example.miaosha.service.MiaoshaUserService;
import com.example.miaosha.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        if (handler instanceof HandlerMethod){
            MiaoshaUser miaoshaUser = getUser(request, response);
            HandlerMethod handlerMethod = (HandlerMethod) handler;

        }
        return true;
    }

    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response){
        String paramToken = request.getParameter(MiaoshaUserServiceImpl.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserServiceImpl.COOKI_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return miaoshaUserService.getByToken(response, token);

    }
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[]  cookies = request.getCookies();
        if (cookies == null ) return null;
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
