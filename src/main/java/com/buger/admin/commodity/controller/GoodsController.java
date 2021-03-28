package com.buger.admin.commodity.controller;

import com.buger.admin.commodity.po.Goods;
import com.buger.admin.commodity.service.GoodsService;
import com.buger.admin.user.po.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/goods")
public class GoodsController {
    @Autowired(required = false)
    private HttpServletRequest request;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("all")
    public Object goodsList(int page, int limit) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(Integer.parseInt(request.getHeader("id")));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return goodsService.getGoodsList(adminUser, page, limit);
    }

    @PostMapping("add")
    public Object addGoods(Goods goods) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(Integer.parseInt(request.getHeader("id")));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return goodsService.addGoods(adminUser, goods);
    }

    @PostMapping("get/{id}")
    public Object getGoodsById(@PathVariable("id") int id) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(Integer.parseInt(request.getHeader("id")));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return goodsService.getGoodsById(adminUser, id);
    }

    @PostMapping("rest")
    public Object updateGoods(Goods goods) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(request.getIntHeader("id"));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return goodsService.updateGoods(adminUser, goods);
    }

    @PostMapping("delete")
    public Object deleteGoods(String id) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(request.getIntHeader("id"));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return goodsService.deleteGoods(adminUser, id);
    }

    @PostMapping("fast/enable")
    public Object updateEnable(int id, boolean enable) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(request.getIntHeader("id"));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return goodsService.enableGoods(adminUser, id, enable);
    }
    @PostMapping("fast/stock")
    public Object updateStock(int id, int stock) {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(request.getIntHeader("id"));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return goodsService.stockGoods(adminUser, id, stock);
    }
    @PostMapping("type")
    public Object getTypes() {
        return goodsService.typeGoods();
    }
    @PostMapping("/con/get/{id}")
    public Object consumerGetGoodsById(@PathVariable("id") int id) {
        return goodsService.consumerGetGoodsById(id);
    }
}
