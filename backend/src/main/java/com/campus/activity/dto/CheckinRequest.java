package com.campus.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckinRequest {
    @NotNull(message = "活动编号不能为空")
    private Long activityId;

    @NotBlank(message = "签到码不能为空")
    private String checkinCode;
}
