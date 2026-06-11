package com.campus.activity.vo;

import com.campus.activity.entity.Registration;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RegistrationVO {
    private Long registrationId;
    private Long activityId;
    private String activityTitle;
    private Long studentId;
    private String studentNo;
    private String studentName;
    private LocalDateTime registrationTime;
    private String registrationStatus;
    private String statusText;
    private LocalDateTime cancelTime;

    public static RegistrationVO from(Registration registration) {
        RegistrationVO vo = new RegistrationVO();
        vo.setRegistrationId(registration.getRegistrationId());
        vo.setActivityId(registration.getActivityId());
        vo.setStudentId(registration.getStudentId());
        vo.setRegistrationTime(registration.getRegistrationTime());
        vo.setRegistrationStatus(registration.getRegistrationStatus());
        vo.setStatusText(toStatusText(registration.getRegistrationStatus()));
        vo.setCancelTime(registration.getCancelTime());
        return vo;
    }

    private static String toStatusText(String status) {
        return switch (status == null ? "" : status) {
            case "registered" -> "已报名";
            case "cancelled" -> "已取消";
            case "invalid" -> "无效";
            default -> status;
        };
    }
}
