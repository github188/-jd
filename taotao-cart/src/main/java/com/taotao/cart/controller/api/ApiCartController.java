package com.taotao.cart.controller.api;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Ellen on 2017/7/12.
 */
@Controller
@RequestMapping("api/cart")
public class ApiCartController {

    @Autowired
    private CartService cartService;

    /**
     * 根据用户id查询用户购物表
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<Cart>> queryCartList(@PathVariable("userId") long userId) {
        try {
            List<Cart> res = cartService.queryList(userId);
            if (res == null || res.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
