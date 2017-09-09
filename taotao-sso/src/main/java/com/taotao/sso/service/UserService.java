package com.taotao.sso.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Ellen on 2017/7/8.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int REDIS_TIME = 60 * 60 * 3;

    public Boolean checkParam(String param, int type) {
        if (type < 1 || type > 3) {
            return null;
        }
        User user = new User();
        switch (type) {
            case 1:
                user.setUsername(param);
                break;
            case 2:
                user.setPhone(param);
                break;
            case 3:
                user.setEmail(param);
                break;
            default:
                return null;
        }
        //为了兼容前端js逻辑
        return userMapper.selectOne(user) != null;
    }

    public Boolean doRegister(User user) {
        User one = new User();
        one.setUsername(user.getUsername());
        one = userMapper.selectOne(one);
        if (one != null) {
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        //加密密码
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        int res = userMapper.insert(user);
        return res == 1;
    }

    public String doLogin(String userName, String password) throws Exception {
        User user = new User();
        user.setUsername(userName);
        User user1 = userMapper.selectOne(user);
        if (user1 == null) {
            return null;
        }
        //对比密码是否相同
        if (!user1.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return null;
        }
        //登录成功
        String token = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + userName).getBytes());
        user1.setPassword(null);
        redisService.setExpire("TOKEN_" + token, objectMapper.writeValueAsString(user1), REDIS_TIME);
        return token;
    }

    public User queryUserBytoken(String token) {
        String key = "TOKEN_" + token;
        String data = redisService.get(key);
        if (data == null) {
            //登陆超时
            return null;
        }
        redisService.setExpire(key, data, REDIS_TIME);
        try {
            return objectMapper.readValue(data, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logout(String token) {
        String key = "TOKEN_" + token;
        redisService.delete(key);
    }
}
