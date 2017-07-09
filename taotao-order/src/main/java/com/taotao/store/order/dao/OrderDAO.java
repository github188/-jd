package com.taotao.store.order.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.taotao.store.order.bean.Where;
import com.taotao.store.order.mapper.OrderMapper;
import com.taotao.store.order.pojo.Order;
import com.taotao.store.order.pojo.PageResult;
import com.taotao.store.order.pojo.ResultMsg;

/**
 * mysql版本的实现
 *
 */
public class OrderDAO implements IOrder {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void createOrder(Order order) {
//        this.orderMapper.save(order);
    }

    @Override
    public Order queryOrderById(String orderId) {
        return null;
    }

    @Override
    public PageResult<Order> queryOrderByUserNameAndPage(String buyerNick, Integer page, Integer count) {
//        PageBounds bounds = new PageBounds();
//        bounds.setContainsTotalCount(true);
//        bounds.setLimit(count);
//        bounds.setPage(page);
//        bounds.setOrders(com.github.miemiedev.mybatis.paginator.domain.Order.formString("create_time.desc"));
//        PageList<Order> list = this.orderMapper
//                .queryListByWhere(bounds, Where.build("buyer_nick", buyerNick));
        return null;
    }

    @Override
    public ResultMsg changeOrderStatus(Order order) {
//        try {
//            order.setUpdateTime(new Date());
//            this.orderMapper.update(order);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResultMsg("500", "更新订单出错!");
//        }
        return null;
    }

}
