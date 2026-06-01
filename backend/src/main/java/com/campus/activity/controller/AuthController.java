package com.campus.activity.controller;

import com.campus.activity.common.ApiResponse;
import com.campus.activity.common.AuthUser;
import com.campus.activity.dto.LoginRequest;
import com.campus.activity.service.AuthService;
import com.campus.activity.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @GetMapping("/users/me")
    public ApiResponse<AuthUser> me(@RequestHeader("Authorization") String token) {
        return ApiResponse.ok(authService.current(token));
    }
}
