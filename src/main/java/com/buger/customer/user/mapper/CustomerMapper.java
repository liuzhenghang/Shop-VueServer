package com.buger.customer.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buger.customer.user.po.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CustomerMapper extends BaseMapper<Customer> {
}
