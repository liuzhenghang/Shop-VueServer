package com.buger.admin.user.controller;

import com.buger.admin.user.po.AdminUser;
import com.buger.admin.user.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/user")
public class AdminUserCtrl {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AdminUserService adminUserService;


    @PostMapping("login")
    public Object Login(String username,String password){
        AdminUser adminUser=new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPassword(password);
        adminUser.setIp(request.getRemoteAddr());
        return adminUserService.login(adminUser);
    }
    @PostMapping("token")
    public Object Token(){
        AdminUser adminUser=new AdminUser();
        adminUser.setId(Integer.parseInt(request.getHeader("id")));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return adminUserService.token(adminUser);
    }
    @GetMapping("all")
    public Object adminList(int page,int limit){
        AdminUser adminUser=new AdminUser();
        adminUser.setId(Integer.parseInt(request.getHeader("id")));
        adminUser.setToken(request.getHeader("token"));
        adminUser.setIp(request.getRemoteAddr());
        return adminUserService.getAdminList(adminUser,page,limit);
    }

}
