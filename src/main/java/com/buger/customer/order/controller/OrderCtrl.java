package com.buger.customer.order.controller;

import com.buger.admin.order.service.AdminOrderService;
import com.buger.customer.order.po.Order;
import com.buger.customer.order.service.OrderService;
import com.buger.system.tools.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("order")
public class OrderCtrl {
    @Autowired
    OrderService orderService;
    @Autowired
    AdminOrderService adminOrderService;

    @Autowired(required = false)
    HttpServletRequest request;



    @PostMapping("push")
    public Object pushNewOrder(@RequestBody Order order){
        order.setUid(request.getIntHeader("id"));
        order.setTime(SHA.getNowTime());
        return orderService.setNewOrder(order);
    }
    @PostMapping("getAll")
    public Object getAllOrder(){
        int id=request.getIntHeader("id");
        return orderService.getAllOrder(id);
    }
    @PostMapping("remove")
    public Object removeOrder(@RequestBody Order order){
        return adminOrderService.removeOrder(order.getId());
    }

}
