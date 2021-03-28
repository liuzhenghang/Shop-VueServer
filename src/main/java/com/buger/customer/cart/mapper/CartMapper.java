package com.buger.customer.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buger.customer.cart.po.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CartMapper extends BaseMapper<Cart> {
}
