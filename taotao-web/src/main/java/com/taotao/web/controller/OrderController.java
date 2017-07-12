package com.taotao.web.controller;

import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.util.LocalUser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ellen on 2017/7/9.
 */
@RequestMapping("order")
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    /**
     * 提交订单
     *
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String toOrder(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemService.queryById(itemId);
        model.addAttribute("item", item);
//        ModelAndView mv = new ModelAndView("order");
//        mv.addObject("item", item);
        return "order";
    }


    /**
     * 确认订单
     *
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submit(Order order, @CookieValue("TT_TOKEN") String token,
                                      HttpServletResponse response) {
        try {
            User user = LocalUser.getUser();
            if (user != null) {
                order.setUserId(user.getId());
                order.setBuyerNick(user.getUsername());
            } else {
                response.sendRedirect("http://sso.taotao.com/user/login.html");
            }
            String orderId = orderService.sumitOrder(order);
            Map<String, Object> res = new HashMap<>();
            if (orderId == null) {
                //订单提交失败
                res.put("status", 400);
            } else {
                //订单提交成功
                res.put("status", 200);
                res.put("data", orderId);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "success", method = RequestMethod.GET)
    public ModelAndView success(@RequestParam("id") String id,
                                Model model) {
        Order order = orderService.queryById(id);

        ModelAndView mv = new ModelAndView("success");
        mv.addObject("order", order);
        mv.addObject("date", new DateTime().plus(2).toString("MM月dd日"));
        return mv;
    }


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView createByCookie() {
        ModelAndView mv = new ModelAndView("order-cart");
        List<Cart> carts = cartService.queryCartList();

        mv.addObject("carts", carts);

        return mv;
    }
}
