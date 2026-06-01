package com.campus.activity.controller;

import com.campus.activity.common.ApiResponse;
import com.campus.activity.dto.RatingRequest;
import com.campus.activity.entity.Rating;
import com.campus.activity.service.AuthService;
import com.campus.activity.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    private final AuthService authService;

    @PostMapping
    public ApiResponse<Rating> rate(@Valid @RequestBody RatingRequest request,
                                    @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(ratingService.rate(request.getActivityId(), request.getScore(), request.getComment(), authService.current(token)));
    }
}
