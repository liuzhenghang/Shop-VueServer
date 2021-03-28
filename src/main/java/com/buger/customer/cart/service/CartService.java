package com.buger.customer.cart.service;

import com.buger.customer.cart.po.Cart;

import java.util.List;

public interface CartService {
    Object getCartByUid(int id);
    Object addGoodsForCart(Cart cart);
    Object removeCart(Cart cart);
    int removeCartById(List<Integer> list);
}
