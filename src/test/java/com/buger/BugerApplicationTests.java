package com.buger;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.buger.admin.user.mapper.AdminUserMapper;
import com.buger.admin.user.po.AdminUser;
import com.buger.system.tools.SHA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BugerApplicationTests {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Test
    void contextLoads() {
    }
    @Test
    void testPassword(){
        String s="0000";
        System.out.println(SHA.sha224(s));
        System.out.println(SHA.md5(s));
    }
    @Test
    void addAdminUser(){
        int row=0;
        for (int i = 0; i < 100; i++) {
            AdminUser adminUser=new AdminUser();
            adminUser.setUsername("addUserTest"+i);
            adminUser.setPassword(SHA.sha224("test"));
            adminUser.setPower(1);
            adminUser.setEnable(true);
            row+=adminUserMapper.insert(adminUser);
        }
        System.out.println("已添加"+row+"个");
    }
    @Test
    void removeAdminUser(){
        int row=0;
        for (int i = 0; i < 100; i++) {
            QueryWrapper<AdminUser> queryWrapper=new QueryWrapper<>();
            queryWrapper.lambda().eq(AdminUser::getUsername,"addUserTest"+i);
            row+=adminUserMapper.delete(queryWrapper);
        }
        System.out.println("已删除"+row+"个");
    }


}
