package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.web.mapper.OrderItemMapper;
import com.taotao.web.mapper.OrderMapper;
import com.taotao.web.mapper.OrderShippingMapper;
import com.taotao.web.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Ellen on 2017/7/10.
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderShippingMapper orderShippingMapper;

    private static final ObjectMapper MAPPER = new ObjectMapper();


    public TaotaoResult createOrder(String json) {
        TheOrder theorder = null;
        try {
            theorder = MAPPER.readValue(json, TheOrder.class);
        } catch (IOException e) {
            e.printStackTrace();
            return TaotaoResult.build(400, "保存订单失败!");
        }
        String orderId = theorder.getUserId() + "" + System.currentTimeMillis();
        theorder.setOrderId(orderId);
        Order order = setOrder(theorder);
        orderMapper.insert(order);
        List<OrderItem> orderItems = theorder.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItemMapper.insert(orderItem);
        }
        OrderShipping orderShipping = theorder.getOrderShipping();
        if (orderShipping != null) {
            orderShipping.setOrderId(orderId);
            orderShippingMapper.insert(orderShipping);
        }
        return TaotaoResult.ok(orderId);
    }

    public Order queryOrderById(String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        Order order1 = orderMapper.selectOne(order);
        return order1 == null ? null : order1;
    }


    private Order setOrder(TheOrder theorder) {
        Order order = new Order();
        order.setOrderId(theorder.getOrderId());
        order.setPayment(theorder.getPayment());
        order.setPaymentType(theorder.getPaymentType());
        order.setPostFee(theorder.getPostFee());

        order.setStatus(theorder.getStatus());
        order.setCreateTime(theorder.getCreateTime());
        order.setUpdateTime(theorder.getUpdateTime());
        order.setPaymentTime(theorder.getPaymentTime());

        order.setConsignTime(theorder.getConsignTime());
        order.setEndTime(theorder.getEndTime());
        order.setCloseTime(theorder.getCloseTime());
        order.setShippingName(theorder.getShippingName());

        order.setShippingCode(theorder.getShippingCode());
        order.setUserId(theorder.getUserId());
        order.setBuyerMessage(theorder.getBuyerMessage());
        order.setBuyerNick(theorder.getBuyerNick());

        order.setBuyerRate(theorder.getBuyerRate());

        return order;
    }
}
