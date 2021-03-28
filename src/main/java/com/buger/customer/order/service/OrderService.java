package com.buger.customer.order.service;

import com.buger.customer.order.po.Order;

public interface OrderService {
    Object setNewOrder(Order order);
    Object getAllOrder(int id);
    Object removeOrder(int id);
}
