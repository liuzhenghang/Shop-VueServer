package com.buger.customer.user.service;

import com.buger.customer.user.po.Customer;

public interface CustomerService {
    Object login(Customer customer);
    Object token(Customer customer);
    boolean catToken(Customer customer);
    Object register(Customer customer);
    Object updateMessage(Customer customer);
}
