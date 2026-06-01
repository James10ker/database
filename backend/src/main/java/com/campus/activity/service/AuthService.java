package com.campus.activity.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.common.AuthUser;
import com.campus.activity.common.BusinessException;
import com.campus.activity.common.TokenStore;
import com.campus.activity.dto.LoginRequest;
import com.campus.activity.entity.Admin;
import com.campus.activity.entity.Organizer;
import com.campus.activity.entity.Student;
import com.campus.activity.mapper.AdminMapper;
import com.campus.activity.mapper.OrganizerMapper;
import com.campus.activity.mapper.StudentMapper;
import com.campus.activity.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final StudentMapper studentMapper;
    private final OrganizerMapper organizerMapper;
    private final AdminMapper adminMapper;
    private final TokenStore tokenStore;

    public LoginVO login(LoginRequest request) {
        AuthUser user = switch (request.getRole().toLowerCase()) {
            case "student" -> loginStudent(request);
            case "organizer" -> loginOrganizer(request);
            case "admin" -> loginAdmin(request);
            default -> throw new BusinessException("不支持的登录角色");
        };
        return new LoginVO(tokenStore.issue(user), user);
    }

    public AuthUser current(String token) {
        return tokenStore.get(token);
    }

    private AuthUser loginStudent(LoginRequest request) {
        Student student = studentMapper.selectOne(new LambdaQueryWrapper<Student>()
                .eq(Student::getStudentNo, request.getUsername())
                .eq(Student::getPassword, request.getPassword()));
        if (student == null) {
            throw new BusinessException("学生账号或密码错误");
        }
        assertEnabled(student.getAccountStatus());
        return new AuthUser(student.getStudentId(), student.getStudentNo(), student.getStudentName(), "student");
    }

    private AuthUser loginOrganizer(LoginRequest request) {
        Organizer organizer = organizerMapper.selectOne(new LambdaQueryWrapper<Organizer>()
                .eq(Organizer::getOrganizerNo, request.getUsername())
                .eq(Organizer::getPassword, request.getPassword()));
        if (organizer == null) {
            throw new BusinessException("组织者账号或密码错误");
        }
        assertEnabled(organizer.getAccountStatus());
        return new AuthUser(organizer.getOrganizerId(), organizer.getOrganizerNo(), organizer.getOrganizerName(), "organizer");
    }

    private AuthUser loginAdmin(LoginRequest request) {
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, request.getUsername())
                .eq(Admin::getPassword, request.getPassword()));
        if (admin == null) {
            throw new BusinessException("管理员账号或密码错误");
        }
        assertEnabled(admin.getAccountStatus());
        return new AuthUser(admin.getAdminId(), admin.getUsername(), admin.getAdminName(), "admin");
    }

    private void assertEnabled(String status) {
        if (!"enabled".equalsIgnoreCase(status)) {
            throw new BusinessException("账号已被禁用");
        }
    }
}
