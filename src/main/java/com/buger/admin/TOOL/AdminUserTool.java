package com.buger.admin.TOOL;

import com.buger.admin.user.po.AdminUser;

public interface AdminUserTool {
    boolean catToken(AdminUser adminUser);
    boolean isSuperAdmin(AdminUser user);
    boolean isGoodsAdmin(AdminUser user);
    boolean isOrderAdmin(AdminUser user);
}
