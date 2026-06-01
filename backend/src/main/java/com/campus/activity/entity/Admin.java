package com.campus.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("admin_user")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long adminId;
    private String username;
    private String password;
    private String adminName;
    private String accountStatus;
    private LocalDateTime createdTime;
}
