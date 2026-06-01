package com.campus.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.common.AuthUser;
import com.campus.activity.common.BusinessException;
import com.campus.activity.entity.Activity;
import com.campus.activity.entity.Checkin;
import com.campus.activity.entity.Registration;
import com.campus.activity.mapper.ActivityMapper;
import com.campus.activity.mapper.CheckinMapper;
import com.campus.activity.mapper.RegistrationMapper;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckinService {
    private final ActivityMapper activityMapper;
    private final RegistrationMapper registrationMapper;
    private final CheckinMapper checkinMapper;
    private final ActivityService activityService;

    public String generateCode(Long activityId, AuthUser user) {
        Activity activity = activityService.detail(activityId);
        activityService.assertOrganizerOwner(activity, user);
        String code = String.valueOf(100000 + new Random().nextInt(900000));
        activity.setCheckinCode(code);
        activity.setUpdatedTime(LocalDateTime.now());
        activityMapper.updateById(activity);
        return code;
    }

    public Checkin checkin(Long activityId, String code, AuthUser user) {
        activityService.requireRole(user, "student");
        Activity activity = activityService.detail(activityId);
        if (activity.getCheckinCode() == null || !activity.getCheckinCode().equals(code)) {
            throw new BusinessException("签到码错误");
        }
        Registration registration = registrationMapper.selectOne(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getActivityId, activityId)
                .eq(Registration::getStudentId, user.getId())
                .eq(Registration::getRegistrationStatus, "registered"));
        if (registration == null) {
            throw new BusinessException("未报名或报名已取消，不能签到");
        }
        return createCheckin(registration, "code", "checked_in", null);
    }

    public Checkin manual(Long registrationId, String remark, AuthUser user) {
        Registration registration = registrationMapper.selectById(registrationId);
        if (registration == null || !"registered".equals(registration.getRegistrationStatus())) {
            throw new BusinessException("有效报名记录不存在");
        }
        Activity activity = activityService.detail(registration.getActivityId());
        activityService.assertOrganizerOwner(activity, user);
        return createCheckin(registration, "manual", "manual_checked_in", remark);
    }

    private Checkin createCheckin(Registration registration, String method, String status, String remark) {
        Checkin existing = checkinMapper.selectOne(new LambdaQueryWrapper<Checkin>()
                .eq(Checkin::getRegistrationId, registration.getRegistrationId()));
        if (existing != null) {
            throw new BusinessException("不能重复签到");
        }
        Checkin checkin = new Checkin();
        checkin.setRegistrationId(registration.getRegistrationId());
        checkin.setActivityId(registration.getActivityId());
        checkin.setStudentId(registration.getStudentId());
        checkin.setCheckinTime(LocalDateTime.now());
        checkin.setCheckinMethod(method);
        checkin.setCheckinStatus(status);
        checkin.setRemark(remark);
        checkinMapper.insert(checkin);
        return checkin;
    }
}
