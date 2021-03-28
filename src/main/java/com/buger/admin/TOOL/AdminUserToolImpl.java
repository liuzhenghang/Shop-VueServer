package com.buger.admin.TOOL;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.buger.admin.user.mapper.AdminUserMapper;
import com.buger.admin.user.po.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminUserToolImpl implements AdminUserTool{
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Override
    public boolean catToken(AdminUser adminUser) {
        QueryWrapper<AdminUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AdminUser::getId,adminUser.getId())
                .eq(AdminUser::getToken,adminUser.getToken());
        adminUser=adminUserMapper.selectOne(queryWrapper);
        if (adminUser==null){
            return false;
        }
        return true;
    }

    @Override
    public boolean isSuperAdmin(AdminUser user) {
        user=adminUserMapper.selectById(user.getId());
        if (user.isSuperAdmin() && user.isEnable()){
            return true;
        }
        return false;
    }

    @Override
    public boolean isGoodsAdmin(AdminUser user) {
        user=adminUserMapper.selectById(user.getId());
        if (user.isGoodsAdmin() && user.isEnable()){
            return true;
        }
        return false;
    }

    @Override
    public boolean isOrderAdmin(AdminUser user) {
        user=adminUserMapper.selectById(user.getId());
        if (user.isOrderAdmin() && user.isEnable()){
            return true;
        }
        return false;
    }

}
