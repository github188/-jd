package com.taotao.cart.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.User;
import com.taotao.cart.util.LocalUser;
import com.taotao.common.service.RedisService;
import com.taotao.common.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Ellen on 2017/7/9.
 */
public class UserLoginHandlerIntercepter implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if (token == null) {
            return true;
        }

        String key = "TOKEN_" + token;
        String data = redisService.get(key);
        if (data == null) {
            return true;
        }
        User user = MAPPER.readValue(data, User.class);

        if (user == null) {
            return true;
        }
        LocalUser.setUser(user);
        //登录成功
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
