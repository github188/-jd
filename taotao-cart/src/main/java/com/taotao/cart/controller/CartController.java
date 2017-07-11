package com.taotao.cart.controller;

import com.taotao.cart.bean.User;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.cart.util.LocalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Ellen on 2017/7/11.
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 加入商品到购物车
     *
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") long itemId) {
        User user = LocalUser.getUser();
        if (user == null) {
            //用户未登录

        } else {
            //登录状态
            cartService.addItemToCart(itemId);
        }
        return "redirect:/cart/list.html";
    }

    /**
     * 查询购物车列表
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView showCartList() {
        ModelAndView mv = new ModelAndView("cart");
        List<Cart> cartList = null;
        User user = LocalUser.getUser();
        if (user == null) {
            //用户未登录

        } else {
            //登录状态
            cartList = cartService.queryList();
        }
        mv.addObject("cartList", cartList);
        return mv;
    }
}
