package com.buger.customer.commodity.service;

import com.buger.admin.commodity.po.Goods;

import java.util.List;

public interface CGoodsService {
    Object GetAllGoodsList(String[] types);
    Object GetGoodsType();
    List<Goods> getGoodsById(List<Integer> list);
    boolean delStork(Goods goods);
}
