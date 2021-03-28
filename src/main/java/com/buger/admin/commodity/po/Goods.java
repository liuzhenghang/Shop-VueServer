package com.buger.admin.commodity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_goods")
public class Goods {
    private int id;
    private String imgs;
    private String name;
    private String description;
    private String type;
    private Double price;
    private Double cost;
    private int stock;
    private int sell;
    private boolean enable;
    private String time;

}
