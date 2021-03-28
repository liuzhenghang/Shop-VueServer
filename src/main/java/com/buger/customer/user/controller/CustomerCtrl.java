package com.buger.customer.user.controller;

import com.buger.customer.user.po.Customer;
import com.buger.customer.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class CustomerCtrl {
    @Autowired
    private CustomerService customerService;
    @Autowired(required = false)
    private HttpServletRequest request;


    @PostMapping("login")
    public Object login(@RequestBody Customer customer){
        System.out.println(customer);
        return customerService.login(customer);
    }
    @GetMapping("token")
    public Object token(Customer customer){
        System.out.println(customer);
        return customerService.token(customer);
    }
    @PostMapping("register")
    public Object register(@RequestBody Customer customer){
        return customerService.register(customer);
    }
    @PostMapping("update")
    public Object update(@RequestBody Customer customer){
        customer.setId(request.getIntHeader("id"));
        return customerService.updateMessage(customer);
    }

}
