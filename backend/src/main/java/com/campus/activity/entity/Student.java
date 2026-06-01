package com.campus.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long studentId;
    private String studentNo;
    private String password;
    private String studentName;
    private String college;
    private String major;
    private String grade;
    private String phone;
    private String email;
    private String interestTags;
    private String accountStatus;
    private LocalDateTime createdTime;
}
