package com.campus.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.common.AuthUser;
import com.campus.activity.common.BusinessException;
import com.campus.activity.entity.Activity;
import com.campus.activity.entity.Registration;
import com.campus.activity.entity.Student;
import com.campus.activity.mapper.ActivityMapper;
import com.campus.activity.mapper.RegistrationMapper;
import com.campus.activity.mapper.StudentMapper;
import com.campus.activity.vo.RegistrationVO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationMapper registrationMapper;
    private final ActivityMapper activityMapper;
    private final StudentMapper studentMapper;
    private final ActivityService activityService;

    public Registration register(Long activityId, AuthUser user) {
        activityService.requireRole(user, "student");
        Activity activity = activityService.detail(activityId);
        LocalDateTime now = LocalDateTime.now();
        if (!activityService.isRegistrationOpen(activity, now)) {
            throw new BusinessException("当前活动不在可报名时间范围内");
        }
        long count = registrationMapper.selectCount(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getActivityId, activityId)
                .eq(Registration::getRegistrationStatus, "registered"));
        if (count >= activity.getCapacity()) {
            throw new BusinessException("活动名额已满");
        }
        Registration existing = registrationMapper.selectOne(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getActivityId, activityId)
                .eq(Registration::getStudentId, user.getId()));
        if (existing != null && "registered".equals(existing.getRegistrationStatus())) {
            throw new BusinessException("不能重复报名同一活动");
        }
        if (hasTimeConflict(user.getId(), activity)) {
            throw new BusinessException("报名活动时间与已报名活动冲突");
        }
        Registration registration = existing == null ? new Registration() : existing;
        registration.setActivityId(activityId);
        registration.setStudentId(user.getId());
        registration.setRegistrationTime(now);
        registration.setRegistrationStatus("registered");
        registration.setCancelTime(null);
        if (registration.getRegistrationId() == null) {
            registrationMapper.insert(registration);
        } else {
            registrationMapper.updateById(registration);
        }
        return registration;
    }

    public Registration cancel(Long id, AuthUser user) {
        activityService.requireRole(user, "student");
        Registration registration = registrationMapper.selectById(id);
        if (registration == null || !registration.getStudentId().equals(user.getId())) {
            throw new BusinessException("报名记录不存在");
        }
        Activity activity = activityService.detail(registration.getActivityId());
        if (LocalDateTime.now().isAfter(activity.getRegisterEndTime())) {
            throw new BusinessException("报名截止后不能取消报名");
        }
        registration.setRegistrationStatus("cancelled");
        registration.setCancelTime(LocalDateTime.now());
        registrationMapper.updateById(registration);
        return registration;
    }

    public List<RegistrationVO> mine(AuthUser user) {
        activityService.requireRole(user, "student");
        return registrationMapper.selectList(new LambdaQueryWrapper<Registration>()
                        .eq(Registration::getStudentId, user.getId())
                        .orderByDesc(Registration::getRegistrationTime))
                .stream()
                .map(this::toVO)
                .toList();
    }

    public List<RegistrationVO> listByActivity(Long activityId, String status, AuthUser user) {
        Activity activity = activityService.detail(activityId);
        activityService.assertOrganizerOwner(activity, user);
        return registrationMapper.selectList(new LambdaQueryWrapper<Registration>()
                        .eq(Registration::getActivityId, activityId)
                        .eq(status != null && !status.isBlank(), Registration::getRegistrationStatus, status)
                        .orderByDesc(Registration::getRegistrationTime))
                .stream()
                .map(this::toVO)
                .toList();
    }

    private RegistrationVO toVO(Registration registration) {
        RegistrationVO vo = RegistrationVO.from(registration);
        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity != null) {
            vo.setActivityTitle(activity.getActivityTitle());
        }
        Student student = studentMapper.selectById(registration.getStudentId());
        if (student != null) {
            vo.setStudentNo(student.getStudentNo());
            vo.setStudentName(student.getStudentName());
        }
        return vo;
    }

    private boolean hasTimeConflict(Long studentId, Activity target) {
        List<Registration> registrations = registrationMapper.selectList(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getStudentId, studentId)
                .eq(Registration::getRegistrationStatus, "registered"));
        for (Registration registration : registrations) {
            Activity activity = activityMapper.selectById(registration.getActivityId());
            if (activity != null && target.getStartTime().isBefore(activity.getEndTime())
                    && target.getEndTime().isAfter(activity.getStartTime())) {
                return true;
            }
        }
        return false;
    }
}
