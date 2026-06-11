package com.campus.activity.vo;

import com.campus.activity.entity.Activity;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ActivityVO {
    private Long activityId;
    private String activityTitle;
    private String category;
    private String description;
    private Long organizerId;
    private String organizerName;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime registerStartTime;
    private LocalDateTime registerEndTime;
    private Integer capacity;
    private String activityStatus;
    private String statusText;
    private String auditStatus;
    private String auditText;
    private String checkinCode;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Long registrationCount;
    private Integer remainingCapacity;

    public static ActivityVO from(Activity activity) {
        ActivityVO vo = new ActivityVO();
        vo.setActivityId(activity.getActivityId());
        vo.setActivityTitle(activity.getActivityTitle());
        vo.setCategory(activity.getCategory());
        vo.setDescription(activity.getDescription());
        vo.setOrganizerId(activity.getOrganizerId());
        vo.setLocation(activity.getLocation());
        vo.setStartTime(activity.getStartTime());
        vo.setEndTime(activity.getEndTime());
        vo.setRegisterStartTime(activity.getRegisterStartTime());
        vo.setRegisterEndTime(activity.getRegisterEndTime());
        vo.setCapacity(activity.getCapacity());
        vo.setActivityStatus(activity.getActivityStatus());
        vo.setAuditStatus(activity.getAuditStatus());
        vo.setCheckinCode(activity.getCheckinCode());
        vo.setCreatedTime(activity.getCreatedTime());
        vo.setUpdatedTime(activity.getUpdatedTime());
        vo.setAuditText(toAuditText(activity.getAuditStatus()));
        return vo;
    }

    private static String toAuditText(String status) {
        return switch (status == null ? "" : status) {
            case "approved" -> "审核通过";
            case "rejected" -> "审核驳回";
            case "pending" -> "待审核";
            default -> status;
        };
    }
}
