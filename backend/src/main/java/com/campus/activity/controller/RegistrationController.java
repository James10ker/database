package com.campus.activity.controller;

import com.campus.activity.common.ApiResponse;
import com.campus.activity.dto.RegistrationRequest;
import com.campus.activity.entity.Registration;
import com.campus.activity.service.AuthService;
import com.campus.activity.service.RegistrationService;
import com.campus.activity.vo.RegistrationVO;
import jakarta.validation.Valid;
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
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final AuthService authService;

    @PostMapping("/api/registrations")
    public ApiResponse<Registration> register(@Valid @RequestBody RegistrationRequest request,
                                              @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(registrationService.register(request.getActivityId(), authService.current(token)));
    }

    @PutMapping("/api/registrations/{id}/cancel")
    public ApiResponse<Registration> cancel(@PathVariable Long id,
                                            @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(registrationService.cancel(id, authService.current(token)));
    }

    @GetMapping("/api/registrations/mine")
    public ApiResponse<List<RegistrationVO>> mine(@RequestHeader("Authorization") String token) {
        return ApiResponse.ok(registrationService.mine(authService.current(token)));
    }

    @GetMapping("/api/activities/{id}/registrations")
    public ApiResponse<List<RegistrationVO>> listByActivity(@PathVariable Long id,
                                                            @RequestParam(required = false) String status,
                                                            @RequestHeader("Authorization") String token) {
        return ApiResponse.ok(registrationService.listByActivity(id, status, authService.current(token)));
    }
}
