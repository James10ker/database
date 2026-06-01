package com.campus.activity.vo;

import com.campus.activity.common.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginVO {
    private String token;
    private AuthUser user;
}
