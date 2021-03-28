package com.buger.customer.cart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.buger.admin.commodity.po.Goods;
import com.buger.customer.cart.mapper.CartMapper;
import com.buger.customer.cart.po.Cart;
import com.buger.customer.commodity.mapper.CGoodsMapper;
import com.buger.customer.commodity.service.CGoodsService;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CGoodsMapper cGoodsMapper;

    @Override
    public Object getCartByUid(int id) {
        LambdaQueryWrapper<Cart> cartLambdaQueryWrapper=new LambdaQueryWrapper<>();
        cartLambdaQueryWrapper
                .eq(Cart::getUid,id);
        List<Cart> carts=cartMapper.selectList(cartLambdaQueryWrapper);

        return Result.success(carts);
    }

    @Override
    public Object addGoodsForCart(Cart cart) {
        System.out.println(cart);
        LambdaQueryWrapper<Cart> cartLambdaQueryWrapper=new LambdaQueryWrapper<>();
        cartLambdaQueryWrapper
                .eq(Cart::getUid,cart.getUid())
                .eq(Cart::getGid,cart.getGid());
        LambdaQueryWrapper<Goods> goodsLambdaQueryWrapper=new LambdaQueryWrapper<>();
        goodsLambdaQueryWrapper
                .eq(Goods::getId,cart.getGid())
                .eq(Goods::isEnable,true)
                .select(Goods::getStock,Goods::getId);
        Goods goods=cGoodsMapper.selectOne(goodsLambdaQueryWrapper);
        if (goods==null){
            return Result.error(CodeMsg.GOODS_NULL);
        }
        Cart cartOld=cartMapper.selectOne(cartLambdaQueryWrapper);
        if (cartOld!=null){
            if (goods.getStock()-(cartOld.getNum()+cart.getNum())>=0){
                cartOld.setNum(cartOld.getNum()+cart.getNum());
                int row=cartMapper.updateById(cartOld);
                if (row>0){
                    return Result.success(cartOld);
                }
                return Result.error(CodeMsg.SERVER_ERROR);
            }
            return Result.error(CodeMsg.GOODS_IS_PUSH_MAX);
        }else {
            if (cart.getNum()<=0){
                return Result.error(CodeMsg.BIND_ERROR);
            }
            if (goods.getStock()-cart.getNum()>=0){
                int row=cartMapper.insert(cart);
                if (row>0){
                    return Result.success(cart);
                }
                return Result.error(CodeMsg.SERVER_ERROR);
            }
            return Result.error(CodeMsg.GOODS_IS_PUSH_MAX);
        }
    }

    @Override
    public Object removeCart(Cart cart) {
        LambdaUpdateWrapper<Cart> cartLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        cartLambdaUpdateWrapper
                .eq(Cart::getId,cart.getId())
                .eq(Cart::getUid,cart.getUid());

        int row=cartMapper.delete(cartLambdaUpdateWrapper);
        if (row>0){
            return Result.success(cart.getId());
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public int removeCartById(List<Integer> list) {
        LambdaUpdateWrapper<Cart> cartLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        cartLambdaUpdateWrapper
                .in(Cart::getId,list);
        return cartMapper.delete(cartLambdaUpdateWrapper);
    }
}
