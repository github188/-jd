package com.taotao.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.pojo.Item;
import com.taotao.common.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ellen on 2017/7/11.
 */
@Service
public class CartCookieService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String COOKIE_NAME = "TT_CART";

    @Autowired
    private ItemService itemService;

    public List<Cart> queryListByCookie(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        if (cookieValue == null) {
            return new ArrayList<Cart>();
        }
        try {
            return MAPPER.readValue(cookieValue,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Cart>();
        }
    }

    public String addItemToCart(long itemId, HttpServletRequest request) {
        List<Cart> cartList = queryListByCookie(request);
        Cart cart = null;
        for (Cart c : cartList) {
            if (c.getItemId() == itemId) {
                cart = c;
                break;
            }
        }
        if (cart == null) {
            Item item = itemService.queryItemBuId(itemId);
            if (item != null) {
                cart = new Cart();
                cart.setItemId(itemId);
                cart.setUpdated(new Date());
                cart.setCreated(new Date());
                cart.setUpdated(cart.getCreated());
                cart.setNum(1);
                cart.setItemImage(item.getImage());
                cart.setItemPrice(item.getPrice());
                cart.setItemTitle(item.getTitle());
                cartList.add(cart);
            } else {
                return null;
            }
        } else {
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
        }
        try {
            return MAPPER.writeValueAsString(cartList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String updateNum(HttpServletRequest request, long itemId, int num) {
        try {
            List<Cart> list = queryListByCookie(request);
            for (Cart cart : list) {
                if (cart.getItemId() == itemId) {
                    cart.setNum(num);
                    break;
                }
            }
            return MAPPER.writeValueAsString(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteCart(HttpServletRequest request, long itemId) {
        try {
            List<Cart> list = queryListByCookie(request);
            for (Cart cart : list) {
                if (cart.getItemId() == itemId) {
                    list.remove(cart);
                    break;
                }
            }
            return MAPPER.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
