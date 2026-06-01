package com.campus.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("organizer")
public class Organizer {
    @TableId(type = IdType.AUTO)
    private Long organizerId;
    private String organizerNo;
    private String password;
    private String organizerName;
    private String organizerType;
    private String contactName;
    private String contactPhone;
    private String accountStatus;
    private LocalDateTime createdTime;
}
