package com.buger.admin.order.controller;

import com.buger.admin.order.service.AdminOrderService;
import com.buger.customer.order.po.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/order")
public class AdminOrderCtrl {
    @Autowired
    AdminOrderService adminOrderService;
    @GetMapping("page")
    public Object getPage(int page,int limit){
        return adminOrderService.getOrderPage(page,limit);
    }
    @PostMapping("remove")
    public Object removeOrder(int id){
        return adminOrderService.removeOrder(id);
    }
    @PostMapping("del")
    public Object delOrder(int id){
        return adminOrderService.delOrder(id);
    }
    @PostMapping("updateAddress")
    public Object updateAddress(Order order){
        return adminOrderService.setAddress(order);
    }
    @PostMapping("get")
    public Object getOne(int id){
        return adminOrderService.getOrderMessage(id);
    }
}
