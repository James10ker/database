package com.campus.activity.common;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TokenStore {
    private final Map<String, AuthUser> users = new ConcurrentHashMap<>();

    public String issue(AuthUser user) {
        String token = UUID.randomUUID().toString().replace("-", "");
        users.put(token, user);
        return token;
    }

    public AuthUser get(String token) {
        if (token == null || token.isBlank()) {
            throw new BusinessException("请先登录");
        }
        AuthUser user = users.get(token.replace("Bearer ", ""));
        if (user == null) {
            throw new BusinessException("登录状态已失效");
        }
        return user;
    }
}
