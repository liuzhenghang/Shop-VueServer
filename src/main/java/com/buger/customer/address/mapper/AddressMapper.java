package com.buger.customer.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buger.customer.address.po.Address;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AddressMapper extends BaseMapper<Address> {
}
