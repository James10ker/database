package com.campus.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("registration")
public class Registration {
    @TableId(type = IdType.AUTO)
    private Long registrationId;
    private Long activityId;
    private Long studentId;
    private LocalDateTime registrationTime;
    private String registrationStatus;
    private LocalDateTime cancelTime;
}
