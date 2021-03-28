package com.buger.admin.order.service;

import com.buger.customer.order.po.Order;

public interface AdminOrderService {
    Object getOrderPage(int page,int limit);
    Object delOrder(int id);
    Object getOrderMessage(int id);
    Object removeOrder(int id);
    Object setAddress(Order order);
}
