package com.taotao.web.util;

import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;

/**
 * Created by Ellen on 2017/7/10.
 */
public class LocalUser {
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    public LocalUser() {
    }

    public static User getUser() {
        return LOCAL.get();
    }

    public static void setUser(User user) {
        LOCAL.set(user);
    }

}
