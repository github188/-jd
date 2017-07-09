package com.taotao.web.controller;

import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
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
    public Map<String, Object> submit(Order order, @CookieValue("TT_TOKEN") String token) {
        User user = userService.queryUserByToken(token);
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());

        Map<String, Object> res = new HashMap<>();
        String orderId = orderService.sumitOrder(order);
        if (orderId == null) {
            //订单提交失败
            res.put("status", 400);
        } else {
            //订单提交成功
            res.put("status", 200);
            res.put("data", orderId);
        }
        return res;
    }

    @RequestMapping(value = "success", method = RequestMethod.GET)
    public String success(@RequestParam("id") long id,
                          Model model) {
//        Item item = itemService.queryById(id);
//        model.addAttribute("item",)
        return "success";
    }
}
