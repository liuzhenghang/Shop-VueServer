package com.buger.system.config;

import com.alibaba.fastjson.JSON;
import com.buger.admin.TOOL.AdminUserTool;
import com.buger.admin.user.po.AdminUser;
import com.buger.customer.user.po.Customer;
import com.buger.customer.user.service.CustomerService;
import com.buger.system.calback.CodeMsg;
import com.buger.system.calback.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

//admin的登录拦截器
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * url访问权限的集合
     */
    //超级用户
    public static final List<String> SUPER_ADMIN_URL  = new ArrayList<String>();
    //商品管理员
    public static final List<String> GOODS_ADMIN_URL  = new ArrayList<String>();
    //订单管理员
    public static final List<String> ORDER_ADMIN_URL  = new ArrayList<String>();
    //任何人可访问
    public static final List<String> PUBLIC_ADMIN_URL  = new ArrayList<String>();

    /**
     * 添加允许上传的文件类型
     */
    static {
        PUBLIC_ADMIN_URL.add("/admin/user/login");
        PUBLIC_ADMIN_URL.add("/admin/user/token");
        GOODS_ADMIN_URL.add("/admin/goods");
        ORDER_ADMIN_URL.add("");
    }
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
//    @Autowired
    private AdminUserTool adminUserTool;
//    @Autowired
    private CustomerService customerService;

    public LoginInterceptor(AdminUserTool adminUserTool,CustomerService customerService) {
        this.adminUserTool = adminUserTool;
        this.customerService=customerService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int id=request.getIntHeader("id");
        String token=request.getHeader("token");
        String author=request.getHeader("author");
        boolean res=false;
        switch (author){
            case "admin":
                AdminUser adminUser=new AdminUser();
                adminUser.setId(id);
                adminUser.setToken(token);
                adminUser.setIp(request.getRemoteAddr());
                res=adminUserTool.catToken(adminUser);
                break;
            case "customer":
                Customer customer=new Customer();
                customer.setId(id);
                customer.setToken(token);
                res=customerService.catToken(customer);
                break;
        }
        log.info("入站url="+request.getServletPath());
        if (!res){
            returnJson(response,JSON.toJSONString(Result.error(CodeMsg.SESSION_ERROR)));
        }
        return res;
    }
    private void returnJson(HttpServletResponse response, String json){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("json/html;charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
