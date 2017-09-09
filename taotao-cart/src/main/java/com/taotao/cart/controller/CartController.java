package com.taotao.cart.controller;

import com.taotao.cart.bean.User;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartService;
import com.taotao.cart.util.LocalUser;
import com.taotao.common.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Created by Ellen on 2017/7/11.
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartCookieService cartCookieService;

    public static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 3;

    /**
     * 加入商品到购物车
     *
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItemToCart(@PathVariable("itemId") long itemId,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        User user = LocalUser.getUser();
        if (user == null) {
            //用户未登录
            String s = cartCookieService.addItemToCart(itemId, request);
            CookieUtils.setCookie(request, response, "TT_CART", s, COOKIE_TIME, true);
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
    public ModelAndView showCartList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("cart");
        List<Cart> cartList = null;
        User user = LocalUser.getUser();
        if (user == null) {
            //用户未登录
            cartList = cartCookieService.queryListByCookie(request);
        } else {
            //登录状态
            cartList = cartService.queryList();
            mv.addObject("user", user);
        }
        mv.addObject("cartList", cartList);
        return mv;
    }

    /**
     * 修改购物车商品的数量
     *
     * @param num
     * @param itemId
     * @return
     */
    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateNum(@PathVariable("num") int num,
                                          @PathVariable("itemId") long itemId,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        User user = LocalUser.getUser();
        if (user == null) {
            String s = cartCookieService.updateNum(request, itemId, num);
            CookieUtils.setCookie(request, response, "TT_CART", s, COOKIE_TIME, true);
        } else {
            cartService.updateNum(num, itemId);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除购物车中某个商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteCart(@PathVariable("itemId") long itemId,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        User user = LocalUser.getUser();
        if (user == null) {
            String s = cartCookieService.deleteCart(request, itemId);
            CookieUtils.setCookie(request, response, "TT_CART", s, COOKIE_TIME, true);
        } else {
            cartService.deleteCart(itemId);
        }

        return "redirect:/cart/list.html";
    }

    /**
     * 清空购物车
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "deleteAll", method = RequestMethod.GET)
    public String deleteAll(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam("orderId") long orderId) {
        User user = LocalUser.getUser();
        if (user == null) {
            CookieUtils.setCookie(request, response, "TT_CART", "", COOKIE_TIME, true);
        } else {
            cartService.deleteAll(user.getId());
        }

        return "redirect:http://www.taotaocloud.shop/order/success.html?id=" + orderId;
    }


}
