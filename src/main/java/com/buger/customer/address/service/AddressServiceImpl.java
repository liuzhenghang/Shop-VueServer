package com.buger.customer.address.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.buger.customer.address.mapper.AddressMapper;
import com.buger.customer.address.po.Address;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    AddressMapper addressMapper;

    @Override
    public Object getMyAddress(int uid) {
        LambdaQueryWrapper<Address> addressLambdaQueryWrapper=new LambdaQueryWrapper<>();
        addressLambdaQueryWrapper
                .eq(Address::getUid,uid);
        List<Address> list=addressMapper.selectList(addressLambdaQueryWrapper);

        return Result.success(list);
    }

    @Override
    public Object updateAddAddress(Address address) {
        if (address.getId()==0){
            int row=addressMapper.insert(address);
            if (row>0){
                setFirstIsDefault(address.getUid());
                return Result.success(address);
            }
            return Result.error(CodeMsg.SERVER_ERROR);
        }else {
            int row=addressMapper.updateById(address);
            if (row>0){
                return Result.success(address);
            }
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }

    @Override
    public Object setAddressIsDefault(Address address) {
        LambdaUpdateWrapper<Address> addressLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        addressLambdaUpdateWrapper
                .eq(Address::getUid,address.getUid())
                .set(Address::isChief,false);
        addressMapper.update(null,addressLambdaUpdateWrapper);
        addressLambdaUpdateWrapper.clear();
        addressLambdaUpdateWrapper
                .eq(Address::getId,address.getId())
                .eq(Address::getUid,address.getUid())
                .set(Address::isChief,true);
        int row=addressMapper.update(null,addressLambdaUpdateWrapper);
        if (row>0){
            return Result.success(row);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Object removeAddress(Address address) {
        LambdaUpdateWrapper<Address> addressLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        addressLambdaUpdateWrapper
                .eq(Address::getUid,address.getUid())
                .eq(Address::getId,address.getId());
        int row=addressMapper.delete(addressLambdaUpdateWrapper);
        if (row>0){
            setFirstIsDefault(address.getUid());
            return Result.success(row);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public void setFirstIsDefault(int uid) {
        LambdaQueryWrapper<Address> addressLambdaQueryWrapper=new LambdaQueryWrapper<>();
        addressLambdaQueryWrapper
                .eq(Address::getUid,uid);
        List<Address> list=addressMapper.selectList(addressLambdaQueryWrapper);
        if (list.size()==0){
            return;
        }
        boolean then=true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChief()){
                then=false;
            }
        }
        if (then){
            Address address=list.get(0);
            setAddressIsDefault(address);
        }

    }

    @Override
    public Object getDefaultAddress(int uid) {
        LambdaQueryWrapper<Address> addressLambdaQueryWrapper=new LambdaQueryWrapper<>();
        addressLambdaQueryWrapper
                .eq(Address::isChief,true)
                .eq(Address::getUid,uid);
        List<Address> addresses=addressMapper.selectList(addressLambdaQueryWrapper);
        if (addresses.size()==0){
            return Result.success("");
        }
        return Result.success(addresses.get(0).getAddress());
    }
}
