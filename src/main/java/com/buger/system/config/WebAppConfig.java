package com.buger.system.config;

import com.buger.admin.TOOL.AdminUserTool;
import com.buger.customer.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    @Autowired
    AdminUserTool adminUserTool;
    @Autowired
    CustomerService customerService;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(adminUserTool,customerService))
                .excludePathPatterns("/admin/user/login")
                .excludePathPatterns("/admin/user/token")
                .excludePathPatterns("/admin/goods/con/get/*")
                .excludePathPatterns("/file/image/*")
                .excludePathPatterns("/customer/*/*/*")
                .excludePathPatterns("/customer/*/*")
                .excludePathPatterns("/customer/*")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/token")
                .excludePathPatterns("/user/register")
                .addPathPatterns("/**");
    }
}
