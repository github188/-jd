package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.web.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Ellen on 2017/7/9.
 */
@Service
public class UserService {

    @Autowired
    private ApiService apiService;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public User queryUserByToken(String token) {
        String key = "TOKEN_" + token;
        String data = redisService.get(key);
        try {
            User user = MAPPER.readValue(data, User.class);
            if (user != null) {
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
