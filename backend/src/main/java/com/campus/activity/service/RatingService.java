package com.campus.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.common.AuthUser;
import com.campus.activity.common.BusinessException;
import com.campus.activity.entity.Activity;
import com.campus.activity.entity.Checkin;
import com.campus.activity.entity.Rating;
import com.campus.activity.mapper.CheckinMapper;
import com.campus.activity.mapper.RatingMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingMapper ratingMapper;
    private final CheckinMapper checkinMapper;
    private final ActivityService activityService;

    public Rating rate(Long activityId, Integer score, String comment, AuthUser user) {
        activityService.requireRole(user, "student");
        Activity activity = activityService.detail(activityId);
        if (LocalDateTime.now().isBefore(activity.getEndTime())) {
            throw new BusinessException("活动结束后才能评分");
        }
        Checkin checkin = checkinMapper.selectOne(new LambdaQueryWrapper<Checkin>()
                .eq(Checkin::getActivityId, activityId)
                .eq(Checkin::getStudentId, user.getId()));
        if (checkin == null) {
            throw new BusinessException("只有已签到学生才能评分");
        }
        Rating existing = ratingMapper.selectOne(new LambdaQueryWrapper<Rating>()
                .eq(Rating::getActivityId, activityId)
                .eq(Rating::getStudentId, user.getId()));
        if (existing != null) {
            throw new BusinessException("不能重复评分");
        }
        Rating rating = new Rating();
        rating.setActivityId(activityId);
        rating.setStudentId(user.getId());
        rating.setCheckinId(checkin.getCheckinId());
        rating.setScore(score);
        rating.setComment(comment);
        rating.setRatingTime(LocalDateTime.now());
        ratingMapper.insert(rating);
        return rating;
    }
}
