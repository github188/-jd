package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.User;
import com.taotao.web.util.LocalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Ellen on 2017/7/12.
 */
@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    @Value("${CART_URL}")
    private String CART_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public List<Cart> queryCartList() {
        try {
            User user = LocalUser.getUser();
            String url =CART_URL + "/service/api/cart/" + user.getId();
            String jsonData = apiService.doGet(url);
            if (jsonData == null) {
                return null;
            }
            return MAPPER.readValue(jsonData, MAPPER.getTypeFactory().
                    constructCollectionType(List.class, Cart.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
