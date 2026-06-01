package com.campus.activity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotNull(message = "活动编号不能为空")
    private Long activityId;
}
