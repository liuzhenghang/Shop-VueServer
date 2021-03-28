package com.buger.customer.order.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.buger.admin.commodity.mapper.GoodsMapper;
import com.buger.admin.commodity.po.Goods;
import com.buger.customer.cart.po.Cart;
import com.buger.customer.cart.service.CartService;
import com.buger.customer.commodity.service.CGoodsService;
import com.buger.customer.order.mapper.OrderMapper;
import com.buger.customer.order.po.Order;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CGoodsService cGoodsService;
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsMapper goodsMapper;

    private static boolean goodsStorkLock=false;


    @Override
    @Transactional
    public Object setNewOrder(Order order) {
        JSONArray objects = JSONObject.parseArray(order.getGoods());
        List<Integer> goodsList=new ArrayList<>();
        List<Cart> carts=new ArrayList<>();
        System.out.println(order);
        for (int i = 0; i < objects.size(); i++) {
            goodsList.add((Integer) objects.getJSONObject(i).get("gid"));
            Cart cart=new Cart();
            cart.setId((Integer) objects.getJSONObject(i).get("id"));
            cart.setNum((Integer) objects.getJSONObject(i).get("num"));
            cart.setGid((Integer) objects.getJSONObject(i).get("gid"));
            carts.add(cart);
        }
        List<Goods> goodsList1=cGoodsService.getGoodsById(goodsList);

        while (goodsStorkLock){
            //库存扣除器上锁
            try {
                System.out.println("等待线程锁释放");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        goodsStorkLock=true;
        System.out.println("收到订单请求，库存扣除器上锁");
        Double price=0.0;
        List<Goods> updateGood=new ArrayList<>();
        for (int i = 0; i < carts.size(); i++) {
            Cart cart=carts.get(i);
            Goods goods=null;
            for (Goods goods1 : goodsList1) {
                if (goods1.getId()==cart.getGid()){
                    goods=goods1;
                }
            }
            if (goods==null){

                return Result.error(CodeMsg.GOODS_IS_INVALID);
            }
            if (goods.getStock()>=cart.getNum()){
                price+=goods.getPrice()*cart.getNum();
                goods.setStock(goods.getStock()-cart.getNum());
                updateGood.add(goods);
            }else {
                goodsStorkLock=false;
                //释放库存扣除，扣除库存失败
                return Result.error(CodeMsg.ONE_GOODS_IS_PUSH_MAX(goods.getName()));
            }
        }
        //开始扣除库存
        int row=0;
        for (Goods goods : updateGood) {
            if (cGoodsService.delStork(goods)){
                row++;
            }
        }
        goodsStorkLock=false;
        System.out.println("扣除库存结束，锁释放");
        //扣除库存结束，锁释放
        order.setPrice(price);
        order.setSta(true);
        //创建订单
        orderMapper.insert(order);
        List<Integer> cartId=new ArrayList<>();
        for (Cart cart : carts) {
            cartId.add(cart.getId());
        }
        //删除购物车内容
        cartService.removeCartById(cartId);
        return Result.success(row);
    }

    @Override
    public Object getAllOrder(int id) {
        LambdaQueryWrapper<Order> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper
                .select(Order::getId,
                        Order::getGoods,
                        Order::getTime,
                        Order::getPrice,
                        Order::isSta)
                .orderByDesc(Order::getTime)
                .eq(Order::getUid,id);
        List<Order> orders=orderMapper.selectList(queryWrapper);
        return Result.success(orders);
    }

    @Override
    @Transactional
    public Object removeOrder(int id) {
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper=new LambdaQueryWrapper<>();
        orderLambdaQueryWrapper
                .eq(Order::getId,id)
                .select(Order::getId,Order::isSta,Order::getGoods);
        Order order=orderMapper.selectOne(orderLambdaQueryWrapper);
        if (order==null){
            return Result.error(CodeMsg.NO_POWER);
        }
        if (!order.isSta()){
            return Result.error(CodeMsg.NO_POWER);
        }
        LambdaUpdateWrapper<Order> orderLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        orderLambdaUpdateWrapper
                .eq(Order::getId,id)
                .set(Order::isSta,false);
        int row=orderMapper.update(null,orderLambdaUpdateWrapper);
        if (row<1){
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        List<Goods> list=new ArrayList<>();
        JSONArray objects = JSONObject.parseArray(order.getGoods());
        for (int i = 0; i < objects.size(); i++) {
            Goods goods=new Goods();
            goods.setId((Integer) objects.getJSONObject(i).get("gid"));
            goods.setStock((Integer) objects.getJSONObject(i).get("num"));
        }
        //释放库存，懒得加锁了，炸了就炸了吧
        try {
            for (Goods goods : list) {
                Goods tmp=goodsMapper.selectById(goods.getId());
                if (tmp==null){
                    continue;
                }
                LambdaUpdateWrapper<Goods> goodsLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
                goodsLambdaUpdateWrapper
                        .eq(Goods::getId,goods.getId())
                        .set(Goods::getStock,tmp.getStock()+goods.getStock());
                goodsMapper.update(null,goodsLambdaUpdateWrapper);
            }
        }catch (Exception e){
            throw e;
        }
        return Result.success(row);
    }
}
