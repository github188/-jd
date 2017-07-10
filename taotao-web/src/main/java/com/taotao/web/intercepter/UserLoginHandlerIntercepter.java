package com.taotao.web.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.common.util.CookieUtils;
import com.taotao.web.bean.User;
import com.taotao.web.service.UserService;
import com.taotao.web.util.LocalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
            response.sendRedirect("http://sso.taotao.com/user/login.html");
            return false;
        }

        String key = "TOKEN_" + token;
        String data = redisService.get(key);
        if (data == null) {
            response.sendRedirect("http://sso.taotao.com/user/login.html");
            return false;
        }
        User user = MAPPER.readValue(data, User.class);

        if (user == null) {
            response.sendRedirect("http://sso.taotao.com/user/login.html");
            return false;
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
