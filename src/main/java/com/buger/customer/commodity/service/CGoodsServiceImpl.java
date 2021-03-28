package com.buger.customer.commodity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.buger.admin.commodity.po.Goods;
import com.buger.customer.commodity.mapper.CGoodsMapper;
import com.buger.system.calback.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CGoodsServiceImpl implements CGoodsService {
    @Autowired
    private CGoodsMapper CGoodsMapper;

    @Override
    public Object GetAllGoodsList(String[] types) {
        LambdaQueryWrapper<Goods> goodsLambdaQueryWrapper=new LambdaQueryWrapper<>();
        List<String> list=new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            if (!types[i].equals("")){
                list.add(types[i]);
            }
        }
        goodsLambdaQueryWrapper
                .select(Goods::getId,Goods::getName,Goods::getPrice,Goods::getImgs,Goods::getType,Goods::getStock)
                .eq(Goods::isEnable,true);
        if (list.size()>0){
            goodsLambdaQueryWrapper.like(Goods::getType,list.get(0));
        }
        for (int i=1;i<list.size();i++){
            goodsLambdaQueryWrapper
                    .or()
                    .like(Goods::getType,list.get(i));
        }
        List<Goods> goods= CGoodsMapper.selectList(goodsLambdaQueryWrapper);
        return Result.success(goods);
    }

    @Override
    public Object GetGoodsType() {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Goods::isEnable,true)
                .select(Goods::getType);
        List<Goods> goods = CGoodsMapper.selectList(queryWrapper);
        List<String> type=new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            String[] types = goods.get(i).getType().split(";");
            for (int i1 = 0; i1 < types.length; i1++) {
                type.add(types[i1]);
            }
        }
        HashSet set=new HashSet(type);
        type.clear();
        type.addAll(set);

        return Result.success(type);
    }

    @Override
    public List<Goods> getGoodsById(List<Integer> list) {
        LambdaQueryWrapper<Goods> goodsLambdaQueryWrapper=new LambdaQueryWrapper<>();
        goodsLambdaQueryWrapper
                .in(Goods::getId,list)
                .select(Goods::getStock,Goods::getPrice,Goods::getId,Goods::getName);

        return CGoodsMapper.selectList(goodsLambdaQueryWrapper);
    }

    @Override
    public boolean delStork(Goods goods) {
        LambdaUpdateWrapper<Goods> goodsLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        goodsLambdaUpdateWrapper
                .eq(Goods::getId,goods.getId())
                .set(Goods::getStock,goods.getStock());
        if (CGoodsMapper.update(null,goodsLambdaUpdateWrapper)>0){
            return true;
        }
        return false;
    }
}
