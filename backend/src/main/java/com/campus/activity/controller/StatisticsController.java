package com.campus.activity.controller;

import com.campus.activity.common.ApiResponse;
import com.campus.activity.service.StatisticsService;
import com.campus.activity.vo.StatisticsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/activity/{id}")
    public ApiResponse<StatisticsVO> activity(@PathVariable Long id) {
        return ApiResponse.ok(statisticsService.activityStatistics(id));
    }
}
