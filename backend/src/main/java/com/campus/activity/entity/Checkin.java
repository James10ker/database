package com.campus.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("checkin")
public class Checkin {
    @TableId(type = IdType.AUTO)
    private Long checkinId;
    private Long registrationId;
    private Long activityId;
    private Long studentId;
    private LocalDateTime checkinTime;
    private String checkinMethod;
    private String checkinStatus;
    private String remark;
}
