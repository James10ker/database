package com.campus.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.entity.Checkin;
import com.campus.activity.entity.Rating;
import com.campus.activity.entity.Registration;
import com.campus.activity.mapper.CheckinMapper;
import com.campus.activity.mapper.RatingMapper;
import com.campus.activity.mapper.RegistrationMapper;
import com.campus.activity.vo.StatisticsVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final RegistrationMapper registrationMapper;
    private final CheckinMapper checkinMapper;
    private final RatingMapper ratingMapper;

    public StatisticsVO activityStatistics(Long activityId) {
        long registrations = registrationMapper.selectCount(new LambdaQueryWrapper<Registration>()
                .eq(Registration::getActivityId, activityId)
                .eq(Registration::getRegistrationStatus, "registered"));
        long checkins = checkinMapper.selectCount(new LambdaQueryWrapper<Checkin>()
                .eq(Checkin::getActivityId, activityId));
        List<Rating> ratings = ratingMapper.selectList(new LambdaQueryWrapper<Rating>()
                .eq(Rating::getActivityId, activityId));
        BigDecimal avg = ratings.isEmpty()
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(ratings.stream().mapToInt(Rating::getScore).average().orElse(0))
                        .setScale(2, RoundingMode.HALF_UP);

        StatisticsVO vo = new StatisticsVO();
        vo.setActivityId(activityId);
        vo.setRegistrationCount(registrations);
        vo.setCheckinCount(checkins);
        vo.setCheckinRate(registrations == 0 ? BigDecimal.ZERO
                : BigDecimal.valueOf(checkins * 100.0 / registrations).setScale(2, RoundingMode.HALF_UP));
        vo.setRatingCount(ratings.size());
        vo.setAverageScore(avg);
        return vo;
    }
}
