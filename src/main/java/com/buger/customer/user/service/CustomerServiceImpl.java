package com.buger.customer.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.buger.customer.user.mapper.CustomerMapper;
import com.buger.customer.user.po.Customer;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import com.buger.system.tools.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public Object login(Customer customer) {
        customer.setPassword(SHA.md5(customer.getPassword()));
        LambdaQueryWrapper<Customer> customerLambdaQueryWrapper=new LambdaQueryWrapper<>();
        customerLambdaQueryWrapper
                .eq(Customer::getNum,customer.getNum())
                .eq(Customer::getPassword,customer.getPassword())
                .select(Customer::getId,Customer::getName);
        customer=customerMapper.selectOne(customerLambdaQueryWrapper);
        if (customer==null){
            return Result.error(CodeMsg.USER_PASSWORD_ERROR);
        }
        String token=SHA.getDataMD5();
        LambdaUpdateWrapper<Customer> customerLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        customerLambdaUpdateWrapper
                .eq(Customer::getId,customer.getId())
                .set(Customer::getToken,token);
        int row=customerMapper.update(null,customerLambdaUpdateWrapper);
        if (row<1){
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        customer.setToken(token);
        return Result.success(customer);
    }

    @Override
    public Object token(Customer customer) {
        LambdaQueryWrapper<Customer> customerLambdaQueryWrapper=new LambdaQueryWrapper<>();
        customerLambdaQueryWrapper
                .eq(Customer::getToken,customer.getToken())
                .eq(Customer::getId,customer.getId())
                .select(Customer::getId,Customer::getToken,Customer::getName);
        customer=customerMapper.selectOne(customerLambdaQueryWrapper);
        if (customer==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        return Result.success(customer);
    }

    @Override
    public boolean catToken(Customer customer) {
        LambdaQueryWrapper<Customer> customerLambdaQueryWrapper=new LambdaQueryWrapper<>();
        customerLambdaQueryWrapper
                .eq(Customer::getToken,customer.getToken())
                .eq(Customer::getId,customer.getId())
                .select(Customer::getId,Customer::getToken,Customer::getName);
        customer=customerMapper.selectOne(customerLambdaQueryWrapper);
        if (customer==null){
            return false;
        }
        return true;
    }

    @Override
    public Object register(Customer customer) {
        if (customer.getName()==null || customer.getNum()==null){
            return Result.error(CodeMsg.BIND_ERROR);
        }
        LambdaQueryWrapper<Customer> customerLambdaQueryWrapper=new LambdaQueryWrapper<>();
        customerLambdaQueryWrapper
                .eq(Customer::getNum,customer.getNum());
        int size=customerMapper.selectList(customerLambdaQueryWrapper).size();
        if (size>0){
            return Result.error(CodeMsg.USER_HAS_SAVE);
        }
        customer.setPassword(SHA.md5(customer.getPassword()));
        int rw=customerMapper.insert(customer);
        if (rw>0){
            return Result.success(rw);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Object updateMessage(Customer customer) {
        LambdaUpdateWrapper<Customer> customerLambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        customerLambdaUpdateWrapper
                .eq(Customer::getId,customer.getId())
                .set(Customer::getName,customer.getName());
        if (customer.getPassword()!=null){
            customer.setPassword(SHA.md5(customer.getPassword()));
            customerLambdaUpdateWrapper
                    .set(Customer::getPassword,customer.getPassword());
        }
        int row=customerMapper.update(null,customerLambdaUpdateWrapper);
        if (row>0){
            return Result.success(row);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }
}
