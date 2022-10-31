package com.awen.order.service;

import com.awen.feign.clients.UserClient;
import com.awen.feign.entity.User;
import com.awen.order.entity.Order;
import com.awen.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long orderId) {
        //查询订单
        Order order = orderMapper.findById(orderId);
        //feign远程调用查询用户info
        User user = userClient.findById(order.getUserId());
        //封装user对象
        order.setUser(user);
        //返回
        return order;
    }
}
