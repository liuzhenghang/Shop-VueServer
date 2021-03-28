package com.buger.customer.cart.controller;

import com.buger.customer.cart.po.Cart;
import com.buger.customer.cart.service.CartService;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cart")
public class CartCtrl {
    @Autowired
    private CartService cartService;
    @Autowired(required = false)
    HttpServletRequest request;

    @GetMapping("my")
    public Object getCartByUid(){
        int id=request.getIntHeader("id");
        if (id==0){
            return Result.error(CodeMsg.BIND_ERROR);
        }
        return cartService.getCartByUid(id);
    }
    @PostMapping("add")
    public Object addCart(@RequestBody Cart cart){
        cart.setUid(request.getIntHeader("id"));
        return cartService.addGoodsForCart(cart);
    }
    @PostMapping("remove")
    public Object removeCart(@RequestBody  Cart cart){
        cart.setUid(request.getIntHeader("id"));
        return cartService.removeCart(cart);
    }

}
