package com.campus.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.common.AuthUser;
import com.campus.activity.common.BusinessException;
import com.campus.activity.entity.Activity;
import com.campus.activity.entity.Organizer;
import com.campus.activity.entity.Registration;
import com.campus.activity.mapper.ActivityMapper;
import com.campus.activity.mapper.OrganizerMapper;
import com.campus.activity.mapper.RegistrationMapper;
import com.campus.activity.vo.ActivityVO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityMapper activityMapper;
    private final OrganizerMapper organizerMapper;
    private final RegistrationMapper registrationMapper;

    public List<ActivityVO> list(String keyword, String category, String status, Long organizerId) {
        LambdaQueryWrapper<Activity> query = new LambdaQueryWrapper<>();
        query.like(StringUtils.hasText(keyword), Activity::getActivityTitle, keyword)
                .eq(StringUtils.hasText(category), Activity::getCategory, category)
                .eq(organizerId != null, Activity::getOrganizerId, organizerId)
                .orderByDesc(Activity::getStartTime);
        return activityMapper.selectList(query).stream()
                .map(this::toVO)
                .filter(activity -> !StringUtils.hasText(status) || status.equals(activity.getActivityStatus()))
                .toList();
    }

    public Activity detail(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        return activity;
    }

    public ActivityVO detailVO(Long id) {
        return toVO(detail(id));
    }

    public Activity create(Activity activity, AuthUser user) {
        requireRole(user, "organizer");
        validateActivity(activity);
        activity.setOrganizerId(user.getId());
        activity.setActivityStatus("draft");
        activity.setAuditStatus("pending");
        activity.setCreatedTime(LocalDateTime.now());
        activity.setUpdatedTime(LocalDateTime.now());
        activityMapper.insert(activity);
        return activity;
    }

    public Activity update(Long id, Activity input, AuthUser user) {
        Activity activity = detail(id);
        assertOrganizerOwner(activity, user);
        String computedStatus = computeStatus(activity, LocalDateTime.now());
        if ("ended".equals(computedStatus) || "cancelled".equals(computedStatus)) {
            throw new BusinessException("已结束或已取消活动不能修改");
        }
        input.setActivityId(id);
        input.setOrganizerId(activity.getOrganizerId());
        input.setActivityStatus(activity.getActivityStatus());
        input.setAuditStatus(activity.getAuditStatus());
        input.setCheckinCode(activity.getCheckinCode());
        input.setCreatedTime(activity.getCreatedTime());
        input.setUpdatedTime(LocalDateTime.now());
        validateActivity(input);
        activityMapper.updateById(input);
        return detail(id);
    }

    public Activity publish(Long id, AuthUser user) {
        Activity activity = detail(id);
        assertOrganizerOwner(activity, user);
        activity.setActivityStatus("published");
        activity.setAuditStatus("approved");
        activity.setUpdatedTime(LocalDateTime.now());
        activityMapper.updateById(activity);
        return activity;
    }

    public Activity cancel(Long id, AuthUser user) {
        Activity activity = detail(id);
        assertOrganizerOwner(activity, user);
        activity.setActivityStatus("cancelled");
        activity.setUpdatedTime(LocalDateTime.now());
        activityMapper.updateById(activity);
        return activity;
    }

    public Activity audit(Long id, String auditStatus, AuthUser user) {
        requireRole(user, "admin");
        Activity activity = detail(id);
        activity.setAuditStatus(auditStatus);
        if ("rejected".equals(auditStatus)) {
            activity.setActivityStatus("cancelled");
        }
        activity.setUpdatedTime(LocalDateTime.now());
        activityMapper.updateById(activity);
        return activity;
    }

    public boolean isRegistrationOpen(Activity activity, LocalDateTime now) {
        return "approved".equals(activity.getAuditStatus())
                && !"cancelled".equals(activity.getActivityStatus())
                && !now.isBefore(activity.getRegisterStartTime())
                && !now.isAfter(activity.getRegisterEndTime());
    }

    public boolean isCheckinOpen(Activity activity, LocalDateTime now) {
        return "approved".equals(activity.getAuditStatus())
                && !"cancelled".equals(activity.getActivityStatus())
                && !now.isBefore(activity.getStartTime().minusMinutes(30))
                && !now.isAfter(activity.getEndTime());
    }

    public String computeStatus(Activity activity, LocalDateTime now) {
        if ("cancelled".equals(activity.getActivityStatus())) {
            return "cancelled";
        }
        if ("draft".equals(activity.getActivityStatus())) {
            return "draft";
        }
        if (!"approved".equals(activity.getAuditStatus())) {
            return "pending_review";
        }
        if (now.isAfter(activity.getEndTime())) {
            return "ended";
        }
        if (!now.isBefore(activity.getStartTime()) && !now.isAfter(activity.getEndTime())) {
            return "ongoing";
        }
        if (now.isAfter(activity.getRegisterEndTime())) {
            return "registration_closed";
        }
        if (!now.isBefore(activity.getRegisterStartTime()) && !now.isAfter(activity.getRegisterEndTime())) {
            return "registering";
        }
        return "published";
    }

    public ActivityVO toVO(Activity activity) {
        ActivityVO vo = ActivityVO.from(activity);
        Organizer organizer = organizerMapper.selectById(activity.getOrganizerId());
        if (organizer != null) {
            vo.setOrganizerName(organizer.getOrganizerName());
        }
        long registrationCount = registrationMapper.selectCount(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getActivityId, activity.getActivityId())
                .eq(Registration::getRegistrationStatus, "registered"));
        vo.setRegistrationCount(registrationCount);
        vo.setRemainingCapacity(Math.max(0, activity.getCapacity() - (int) registrationCount));
        vo.setActivityStatus(computeStatus(activity, LocalDateTime.now()));
        vo.setStatusText(toStatusText(vo.getActivityStatus()));
        return vo;
    }

    public void assertOrganizerOwner(Activity activity, AuthUser user) {
        requireRole(user, "organizer");
        if (!activity.getOrganizerId().equals(user.getId())) {
            throw new BusinessException("只能管理自己发布的活动");
        }
    }

    public void requireRole(AuthUser user, String role) {
        if (!role.equals(user.getRole())) {
            throw new BusinessException("当前角色无权限执行该操作");
        }
    }

    private void validateActivity(Activity activity) {
        if (activity.getEndTime() == null || activity.getStartTime() == null
                || !activity.getEndTime().isAfter(activity.getStartTime())) {
            throw new BusinessException("活动结束时间必须晚于开始时间");
        }
        if (activity.getRegisterEndTime() == null || activity.getRegisterStartTime() == null
                || activity.getRegisterEndTime().isAfter(activity.getStartTime())) {
            throw new BusinessException("报名截止时间不能晚于活动开始时间");
        }
        if (activity.getCapacity() == null || activity.getCapacity() <= 0) {
            throw new BusinessException("人数上限必须为正整数");
        }
    }

    private String toStatusText(String status) {
        return switch (status == null ? "" : status) {
            case "draft" -> "草稿";
            case "published" -> "已发布";
            case "registering" -> "报名中";
            case "registration_closed" -> "报名截止";
            case "ongoing" -> "进行中";
            case "ended" -> "已结束";
            case "cancelled" -> "已取消";
            case "pending_review" -> "待审核";
            default -> status;
        };
    }
}
