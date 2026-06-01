package com.campus.activity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManualCheckinRequest {
    @NotNull(message = "报名编号不能为空")
    private Long registrationId;

    private String remark;
}
