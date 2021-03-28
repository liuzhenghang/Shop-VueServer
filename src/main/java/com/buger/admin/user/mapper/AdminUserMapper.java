package com.buger.admin.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.buger.admin.user.po.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
