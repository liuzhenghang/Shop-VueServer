package com.buger.customer.address.controller;

import com.buger.customer.address.po.Address;
import com.buger.customer.address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("address")
public class AddressCtrl {
    @Autowired
    AddressService addressService;
    @Autowired(required = false)
    HttpServletRequest request;

    @GetMapping("my")
    public Object getMyAddress(){
        int uid=request.getIntHeader("id");
        return addressService.getMyAddress(uid);
    }
    @PostMapping("update")
    public Object updateORAdd(@RequestBody Address address){
        int uid=request.getIntHeader("id");
        address.setUid(uid);
        return addressService.updateAddAddress(address);
    }
    @PostMapping("default")
    public Object defaultAddress(@RequestBody Address address){
        int uid=request.getIntHeader("id");
        address.setUid(uid);
        return addressService.setAddressIsDefault(address);
    }
    @PostMapping("remove")
    public Object removeAddress(@RequestBody Address address){
        int uid=request.getIntHeader("id");
        address.setUid(uid);
        return addressService.removeAddress(address);
    }
    @GetMapping("getDefault")
    public Object getDefaultAddress(){
        int uid=request.getIntHeader("id");
        return addressService.getDefaultAddress(uid);
    }


}
