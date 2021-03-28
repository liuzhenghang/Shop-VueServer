package com.buger.admin.user.service;

import com.buger.admin.user.po.AdminUser;

public interface AdminUserService {
    Object login(AdminUser adminUser);
    Object token(AdminUser adminUser);
    Object getAdminList(AdminUser adminUser,int page,int limit);
}
