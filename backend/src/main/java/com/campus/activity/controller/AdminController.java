package com.campus.activity.controller;

import com.campus.activity.common.ApiResponse;
import com.campus.activity.entity.Activity;
import com.campus.activity.service.ActivityService;
import com.campus.activity.service.AdminService;
import com.campus.activity.service.AuthService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final ActivityService activityService;
    private final AuthService authService;

    @GetMapping("/users")
    public ApiResponse<Map<String, Object>> users(@RequestHeader("Authorization") String token) {
        return ApiResponse.ok(adminService.users(authService.current(token)));
    }

    @PutMapping("/users/{type}/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable String type,
                                          @PathVariable Long id,
                                          @RequestBody Map<String, String> body,
                                          @RequestHeader("Authorization") String token) {
        adminService.updateStatus(type, id, body.getOrDefault("status", "enabled"), authService.current(token));
        return ApiResponse.ok(null);
    }

    @PutMapping("/activities/{id}/audit")
    public ApiResponse<Activity> audit(@PathVariable Long id,
                                       @RequestBody Map<String, String> body,
                                       @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(activityService.audit(id, body.getOrDefault("auditStatus", "approved"), authService.current(token)));
    }
}
