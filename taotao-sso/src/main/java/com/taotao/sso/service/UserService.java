package com.taotao.sso.service;

import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;

/**
 * Created by Ellen on 2017/7/8.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

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
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        //加密密码
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        return userMapper.insert(user) == 1;
    }
}
