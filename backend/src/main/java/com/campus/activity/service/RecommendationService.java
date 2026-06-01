package com.campus.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.common.AuthUser;
import com.campus.activity.entity.Activity;
import com.campus.activity.entity.Registration;
import com.campus.activity.mapper.ActivityMapper;
import com.campus.activity.mapper.RegistrationMapper;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final ActivityMapper activityMapper;
    private final RegistrationMapper registrationMapper;
    private final ActivityService activityService;

    public List<Activity> recommend(AuthUser user) {
        activityService.requireRole(user, "student");
        List<Registration> mine = registrationMapper.selectList(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getStudentId, user.getId())
                .eq(Registration::getRegistrationStatus, "registered"));
        Set<Long> registeredIds = new HashSet<>();
        Set<String> categories = new HashSet<>();
        for (Registration registration : mine) {
            registeredIds.add(registration.getActivityId());
            Activity activity = activityMapper.selectById(registration.getActivityId());
            if (activity != null) {
                categories.add(activity.getCategory());
            }
        }
        return activityMapper.selectList(new LambdaQueryWrapper<Activity>()
                        .eq(Activity::getAuditStatus, "approved")
                        .ne(Activity::getActivityStatus, "cancelled")
                        .gt(Activity::getRegisterEndTime, LocalDateTime.now()))
                .stream()
                .filter(activity -> !registeredIds.contains(activity.getActivityId()))
                .sorted(Comparator.comparing((Activity activity) -> categories.contains(activity.getCategory())).reversed()
                        .thenComparing(Activity::getStartTime))
                .limit(10)
                .toList();
    }
}
