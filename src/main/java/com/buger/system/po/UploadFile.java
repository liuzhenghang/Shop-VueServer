package com.buger.system.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_file")
public class UploadFile {
    private int id;
    private String file;
    private int postId;
    private String time;

}
