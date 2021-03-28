package com.buger.admin.order.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.buger.admin.TOOL.PagePO;
import com.buger.admin.commodity.mapper.GoodsMapper;
import com.buger.admin.commodity.po.Goods;
import com.buger.customer.order.mapper.OrderMapper;
import com.buger.customer.order.po.Order;
import com.buger.customer.user.mapper.CustomerMapper;
import com.buger.customer.user.po.Customer;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminOrderServiceImpl implements AdminOrderService{
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public Object getOrderPage(int page, int limit) {
        IPage<Order> pge = new Page<>(page, limit);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .orderByDesc(Order::getTime)
                .select(Order::getId,
                        Order::getPrice,
                        Order::isSta,
                        Order::getTime);
        pge = orderMapper.selectPage(pge, queryWrapper);
        PagePO pagePO = new PagePO((int) pge.getTotal(), pge.getRecords());
        return Result.success(pagePO);
    }

    @Override
    public Object delOrder(int id) {
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper=new LambdaQueryWrapper<>();
        orderLambdaQueryWrapper
                .eq(Order::getId,id)
                .select(Order::getId,Order::isSta);
        Order order=orderMapper.selectOne(orderLambdaQueryWrapper);
        System.out.println("查询订单状态");
        if (order==null){
            return Result.error(CodeMsg.NO_ORDER);
        }
        if (order.isSta()){
            return Result.error(CodeMsg.GOODS_IS_ONLINE);
        }
        int row=orderMapper.deleteById(id);
        if (row>0){
            return Result.success(row);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Object getOrderMessage(int id) {
        Order order=orderMapper.selectById(id);
        if (order==null){
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        Customer customer=customerMapper.selectById(order.getUid());
        Map<String,Object> map=new HashMap<>();
        map.put("order",order);
        if (customer!=null){
            map.put("customer",customer.getName());
        }
        return Result.success(map);
    }

    @Override
    @Transactional
    //注册事务，炸了的话就取消
    public Object removeOrder(int id) {
        LambdaQueryWrapper<Order> orderLambdaQueryWrapper=new LambdaQueryWrapper<>();
        orderLambdaQueryWrapper
                .eq(Order::getId,id)
                .select(Order::getId,Order::isSta,Order::getGoods);
        Order order=orderMapper.selectOne(orderLambdaQueryWrapper);
        System.out.println("查询订单状态");
        if (order==null){
            return Result.error(CodeMsg.NO_ORDER);
        }
        if (!order.isSta()){
            return Result.error(CodeMsg.ORDER_IS_CLEAN);
        }
        LambdaUpdateWrapper<Order> orderLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        orderLambdaUpdateWrapper
                .eq(Order::getId,id)
                .set(Order::isSta,false);
        int row=orderMapper.update(null,orderLambdaUpdateWrapper);
        System.out.println("更新订单信息");
        if (row<1){
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        List<Goods> list=new ArrayList<>();
        JSONArray objects = JSONObject.parseArray(order.getGoods());
        for (int i = 0; i < objects.size(); i++) {
            Goods goods=new Goods();
            goods.setId((Integer) objects.getJSONObject(i).get("gid"));
            goods.setStock((Integer) objects.getJSONObject(i).get("num"));
            list.add(goods);
        }
        System.out.println(objects);
        //释放库存，懒得加锁了，炸了就炸了吧

        for (Goods goods : list) {
            Goods tmp=goodsMapper.selectById(goods.getId());
            System.out.println("订单"+tmp);
            if (tmp==null){
                continue;
            }
            System.out.println(goods);
            LambdaUpdateWrapper<Goods> goodsLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
            goodsLambdaUpdateWrapper
                    .eq(Goods::getId,goods.getId())
                    .set(Goods::getStock,tmp.getStock()+goods.getStock());
            goodsMapper.update(null,goodsLambdaUpdateWrapper);
            System.out.println("更新商品信息");
        }

        return Result.success(row);
    }

    @Override
    public Object setAddress(Order order) {
        LambdaUpdateWrapper<Order> orderLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        orderLambdaUpdateWrapper
                .eq(Order::getId,order.getId())
                .set(Order::getAddress,order.getAddress());
        int row=orderMapper.update(null,orderLambdaUpdateWrapper);
        if (row>0){
            return Result.success(row);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }
}
