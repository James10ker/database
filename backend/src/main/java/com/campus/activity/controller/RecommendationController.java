package com.campus.activity.controller;

import com.campus.activity.common.ApiResponse;
import com.campus.activity.entity.Activity;
import com.campus.activity.service.AuthService;
import com.campus.activity.service.RecommendationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final AuthService authService;

    @GetMapping
    public ApiResponse<List<Activity>> recommend(@RequestHeader("Authorization") String token) {
        return ApiResponse.ok(recommendationService.recommend(authService.current(token)));
    }
}
