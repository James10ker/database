package com.campus.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("rating")
public class Rating {
    @TableId(type = IdType.AUTO)
    private Long ratingId;
    private Long activityId;
    private Long studentId;
    private Long checkinId;
    private Integer score;
    private String comment;
    private LocalDateTime ratingTime;
}
