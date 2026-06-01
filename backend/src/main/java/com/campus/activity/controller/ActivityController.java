package com.campus.activity.controller;

import com.campus.activity.common.ApiResponse;
import com.campus.activity.entity.Activity;
import com.campus.activity.service.ActivityService;
import com.campus.activity.service.AuthService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
    private final AuthService authService;

    @GetMapping
    public ApiResponse<List<Activity>> list(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String status,
                                            @RequestParam(required = false) Long organizerId) {
        return ApiResponse.ok(activityService.list(keyword, category, status, organizerId));
    }

    @GetMapping("/{id}")
    public ApiResponse<Activity> detail(@PathVariable Long id) {
        return ApiResponse.ok(activityService.detail(id));
    }

    @PostMapping
    public ApiResponse<Activity> create(@RequestBody Activity activity,
                                        @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(activityService.create(activity, authService.current(token)));
    }

    @PutMapping("/{id}")
    public ApiResponse<Activity> update(@PathVariable Long id,
                                        @RequestBody Activity activity,
                                        @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(activityService.update(id, activity, authService.current(token)));
    }

    @PutMapping("/{id}/publish")
    public ApiResponse<Activity> publish(@PathVariable Long id,
                                         @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(activityService.publish(id, authService.current(token)));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<Activity> cancel(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(activityService.cancel(id, authService.current(token)));
    }
}
