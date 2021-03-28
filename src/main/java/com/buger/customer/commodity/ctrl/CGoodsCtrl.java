package com.buger.customer.commodity.ctrl;

import com.buger.admin.commodity.service.GoodsService;
import com.buger.customer.commodity.service.CGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer/goods")
public class CGoodsCtrl {
    @Autowired
    private CGoodsService CGoodsService;


    @GetMapping("get/all")
    public Object getAll(String types){
        String[] t={};
        if (types!=null){
            t=types.split(";");
        }
        return CGoodsService.GetAllGoodsList(t);
    }
    @GetMapping("type")
    public Object getAllType(){
        return CGoodsService.GetGoodsType();
    }
}
