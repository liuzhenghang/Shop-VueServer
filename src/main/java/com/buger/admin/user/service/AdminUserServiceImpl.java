package com.buger.admin.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.buger.admin.TOOL.AdminUserTool;
import com.buger.admin.TOOL.PagePO;
import com.buger.admin.user.mapper.AdminUserMapper;
import com.buger.admin.user.po.AdminUser;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import com.buger.system.tools.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;


@Service
public class AdminUserServiceImpl implements AdminUserService{
    @Autowired
    AdminUserMapper adminUserMapper;
    @Autowired
    AdminUserTool adminUserTool;
    @Override
    public Object login(AdminUser adminUser) {
        String ip=adminUser.getIp();
        adminUser.setPassword(SHA.sha224(adminUser.getPassword()));
        QueryWrapper<AdminUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AdminUser::getUsername,adminUser.getUsername())
                .eq(AdminUser::getPassword,adminUser.getPassword());
        adminUser=adminUserMapper.selectOne(queryWrapper);
        if (adminUser==null){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        String token= SHA.getDataSHA224();
        UpdateWrapper<AdminUser> userUpdateWrapper=new UpdateWrapper<>();
        userUpdateWrapper.lambda()
                .eq(AdminUser::getId,adminUser.getId())
                .set(AdminUser::getToken,token)
                .set(AdminUser::getTime,SHA.getNowTime())
                .set(AdminUser::getIp,ip);
        int row=adminUserMapper.update(null,userUpdateWrapper);
        adminUser.setPassword(null);
        adminUser.setToken(token);
        if (row>0){
            return Result.success(adminUser);
        }
        return Result.error(CodeMsg.TOKEN_UPDATE_ERROR);
    }

    @Override
    public Object token(AdminUser adminUser) {
        QueryWrapper<AdminUser> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AdminUser::getId,adminUser.getId())
                .eq(AdminUser::getToken,adminUser.getToken());
        adminUser=adminUserMapper.selectOne(queryWrapper);
        if (adminUser==null){
            return Result.success(-1);
        }
        Instant inst1 = Instant.now();
        Instant inst2 = SHA.toDate(adminUser.getTime()).toInstant();
        long seconds=7200-Duration.between(inst2,inst1).getSeconds();
        if (seconds<=0){
            return Result.success(0);
        }
        return Result.success(seconds);
    }

    @Override
    public Object getAdminList(AdminUser adminUser, int page, int limit) {
//        if (!adminUserTool.catToken(adminUser)){
//            return Result.error(CodeMsg.SESSION_ERROR);
//        }
        if (!adminUserTool.isSuperAdmin(adminUser)){
            return Result.error(CodeMsg.NO_POWER);
        }
        IPage<AdminUser> pge=new Page<>(page,limit);
        LambdaQueryWrapper<AdminUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(
                AdminUser::getId,
                AdminUser::getIp,
                AdminUser::getUsername,
                AdminUser::getPower,
                AdminUser::isEnable,
                AdminUser::getTime);
        pge=adminUserMapper.selectPage(pge,queryWrapper);
        PagePO pagePO=new PagePO((int) pge.getTotal(),pge.getRecords());
        return Result.success(pagePO);
    }
}
