package com.awen.order.service;

import com.awen.order.entity.Order;
import com.awen.order.entity.User;
import com.awen.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RestTemplate restTemplate;

    public Order queryOrderById(Long orderId) {
        //查询订单
        Order order = orderMapper.findById(orderId);
        //url路径
        String url = "http://userservice/user/" + order.getUserId();
        //调用user服务查询用户信息
        User user = restTemplate.getForObject(url, User.class);
        //封装user对象
        order.setUser(user);
        //返回
        return order;
    }
}
