package com.buger.admin.commodity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.buger.admin.TOOL.AdminUserTool;
import com.buger.admin.TOOL.PagePO;
import com.buger.admin.commodity.mapper.GoodsMapper;
import com.buger.admin.commodity.po.Goods;
import com.buger.admin.user.po.AdminUser;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import com.buger.system.tools.SHA;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired(required = false)
    GoodsMapper goodsMapper;
    @Autowired
    AdminUserTool adminUserTool;

    @Override
    public Object getGoodsList(AdminUser adminUser, int page, int limit) {
        if (!adminUserTool.isGoodsAdmin(adminUser)) {
            return Result.error(CodeMsg.NO_POWER);
        }
        IPage<Goods> pge = new Page<>(page, limit);
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Goods::getId,
                Goods::getImgs,
                Goods::getName,
                Goods::getType,
                Goods::getPrice,
                Goods::getCost,
                Goods::getStock,
                Goods::getSell,
                Goods::isEnable,
                Goods::getTime
        );
        pge = goodsMapper.selectPage(pge, queryWrapper);
        PagePO pagePO = new PagePO((int) pge.getTotal(), pge.getRecords());
        return Result.success(pagePO);
    }

    @Override
    public Object addGoods(AdminUser adminUser, Goods goods) {
        if (!adminUserTool.catToken(adminUser)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        if (!adminUserTool.isGoodsAdmin(adminUser)) {
            return Result.error(CodeMsg.NO_POWER);
        }
        goods.setTime(SHA.getNowTime());
        int insert = goodsMapper.insert(goods);
        if (insert > 0) {
            return Result.success(null);
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    @Override
    public Object getGoodsById(AdminUser adminUser, int id) {
        if (!adminUserTool.catToken(adminUser)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        if (!adminUserTool.isGoodsAdmin(adminUser)) {
            return Result.error(CodeMsg.NO_POWER);
        }
        Goods goods = goodsMapper.selectById(id);
        if (goods == null) {
            return Result.error(CodeMsg.DATA_NULL);
        }
        return Result.success(goods);
    }

    @Override
    public Object updateGoods(AdminUser adminUser, Goods goods) {
        if (!adminUserTool.catToken(adminUser)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        if (!adminUserTool.isGoodsAdmin(adminUser)) {
            return Result.error(CodeMsg.NO_POWER);
        }
        int row = goodsMapper.updateById(goods);
        if (row > 0) {
            return Result.success(null);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Object deleteGoods(AdminUser adminUser, String id) {
        if (!adminUserTool.catToken(adminUser)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        if (!adminUserTool.isGoodsAdmin(adminUser)) {
            return Result.error(CodeMsg.NO_POWER);
        }
        String[] uid = id.split(";");
        int row = 0;
        for (int i = 0; i < uid.length; i++) {
            row += goodsMapper.deleteById(uid[i]);
        }
        if (row > 0) {
            return Result.success(row);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Object enableGoods(AdminUser adminUser, int id, boolean enable) {
        if (!adminUserTool.catToken(adminUser)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
//        检查用户登陆状态
        if (!adminUserTool.isGoodsAdmin(adminUser)) {
            return Result.error(CodeMsg.NO_POWER);
        }
//        检查用户权限

        /*
        使用lambda表达式构造SQL语句，加入set和where条件
         */
        LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(Goods::isEnable, enable)
                .eq(Goods::getId, id);
//        执行sql语句
        int row = goodsMapper.update(null, updateWrapper);
        if (row > 0) {
            return Result.success(null);
        }
//        判断SQL语句执行结果，返回响应信息
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Object stockGoods(AdminUser adminUser, int id, int stock) {
        if (!adminUserTool.catToken(adminUser)) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        if (!adminUserTool.isGoodsAdmin(adminUser)) {
            return Result.error(CodeMsg.NO_POWER);
        }
        LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(Goods::getStock, stock)
                .eq(Goods::getId, id);
        int row = goodsMapper.update(null, updateWrapper);
        if (row > 0) {
            return Result.success(null);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Object typeGoods() {
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Goods::getType);
        List<Goods> goods = goodsMapper.selectList(queryWrapper);
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
    public Object consumerGetGoodsById(int id) {
        LambdaQueryWrapper<Goods> goodsLambdaQueryWrapper=new LambdaQueryWrapper<>();
        goodsLambdaQueryWrapper
                .eq(Goods::getId,id)
                .select(Goods::getName,
                        Goods::getPrice,
                        Goods::getStock,
                        Goods::getSell,
                        Goods::isEnable,
                        Goods::getTime,
                        Goods::getType,
                        Goods::getImgs,
                        Goods::getDescription);
        Goods goods = goodsMapper.selectOne(goodsLambdaQueryWrapper);
        if (goods == null) {
            return Result.error(CodeMsg.DATA_NULL);
        }
        return Result.success(goods);
    }

}
