package com.campus.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.common.AuthUser;
import com.campus.activity.common.BusinessException;
import com.campus.activity.entity.Activity;
import com.campus.activity.mapper.ActivityMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityMapper activityMapper;

    public List<Activity> list(String keyword, String category, String status, Long organizerId) {
        LambdaQueryWrapper<Activity> query = new LambdaQueryWrapper<>();
        query.like(StringUtils.hasText(keyword), Activity::getActivityTitle, keyword)
                .eq(StringUtils.hasText(category), Activity::getCategory, category)
                .eq(StringUtils.hasText(status), Activity::getActivityStatus, status)
                .eq(organizerId != null, Activity::getOrganizerId, organizerId)
                .orderByDesc(Activity::getStartTime);
        return activityMapper.selectList(query);
    }

    public Activity detail(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        return activity;
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
        if ("ended".equals(activity.getActivityStatus()) || "cancelled".equals(activity.getActivityStatus())) {
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
}
