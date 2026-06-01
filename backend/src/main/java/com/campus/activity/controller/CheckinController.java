package com.campus.activity.controller;

import com.campus.activity.common.ApiResponse;
import com.campus.activity.dto.CheckinCodeRequest;
import com.campus.activity.dto.CheckinRequest;
import com.campus.activity.dto.ManualCheckinRequest;
import com.campus.activity.entity.Checkin;
import com.campus.activity.service.AuthService;
import com.campus.activity.service.CheckinService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
public class CheckinController {
    private final CheckinService checkinService;
    private final AuthService authService;

    @PostMapping("/code")
    public ApiResponse<Map<String, String>> generateCode(@Valid @RequestBody CheckinCodeRequest request,
                                                         @RequestHeader("Authorization") String token) {
        String code = checkinService.generateCode(request.getActivityId(), authService.current(token));
        return ApiResponse.ok(Map.of("checkinCode", code));
    }

    @PostMapping
    public ApiResponse<Checkin> checkin(@Valid @RequestBody CheckinRequest request,
                                        @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(checkinService.checkin(request.getActivityId(), request.getCheckinCode(), authService.current(token)));
    }

    @PostMapping("/manual")
    public ApiResponse<Checkin> manual(@Valid @RequestBody ManualCheckinRequest request,
                                       @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(checkinService.manual(request.getRegistrationId(), request.getRemark(), authService.current(token)));
    }
}
