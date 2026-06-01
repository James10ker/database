package com.campus.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("activity")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Long activityId;
    private String activityTitle;
    private String category;
    private String description;
    private Long organizerId;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime registerStartTime;
    private LocalDateTime registerEndTime;
    private Integer capacity;
    private String activityStatus;
    private String auditStatus;
    private String checkinCode;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
