package com.buger.admin.commodity.service;

import com.buger.admin.commodity.po.Goods;
import com.buger.admin.user.po.AdminUser;

public interface GoodsService {
    Object getGoodsList(AdminUser adminUser, int page, int limit);

    Object addGoods(AdminUser adminUser, Goods goods);

    Object getGoodsById(AdminUser adminUser, int id);

    Object updateGoods(AdminUser adminUser, Goods goods);

    Object deleteGoods(AdminUser adminUser, String id);

    Object enableGoods(AdminUser adminUser, int id ,boolean enable);
    //快速更改库存
    Object stockGoods(AdminUser adminUser,int id, int stock);
    Object typeGoods();
    Object consumerGetGoodsById(int id);
}
