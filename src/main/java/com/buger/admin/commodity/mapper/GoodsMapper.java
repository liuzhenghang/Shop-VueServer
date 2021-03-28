package com.buger.admin.commodity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buger.admin.commodity.po.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface GoodsMapper extends BaseMapper<Goods> {
}
