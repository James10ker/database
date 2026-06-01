package com.campus.activity.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class StatisticsVO {
    private Long activityId;
    private long registrationCount;
    private long checkinCount;
    private BigDecimal checkinRate;
    private long ratingCount;
    private BigDecimal averageScore;
}
