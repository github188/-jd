package com.taotao.cart.util;


import com.taotao.cart.bean.User;
import com.taotao.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Ellen on 2017/7/10.
 */
public class LocalUser {
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    public LocalUser() {
    }

    public static User getUser() {
        User user = LOCAL.get();
        return LOCAL.get();
    }

    public static void setUser(User user) {
        LOCAL.set(user);
    }

}
