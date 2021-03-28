package com.buger.customer.order.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_order")
@Data
public class Order {
    private int id;
    private int uid;
    private String time;
    private String goods;
    private Double price;
    private String address;
    private boolean sta;
}
