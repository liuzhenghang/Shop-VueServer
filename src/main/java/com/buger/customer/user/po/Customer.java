package com.buger.customer.user.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_customer")
@Data
public class Customer {
    private int id;
    private String num;
    private String name;
    private String password;
    private String token;
}
