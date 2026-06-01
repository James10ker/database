package com.campus.activity.service;

import com.campus.activity.common.AuthUser;
import com.campus.activity.common.BusinessException;
import com.campus.activity.mapper.AdminMapper;
import com.campus.activity.mapper.OrganizerMapper;
import com.campus.activity.mapper.StudentMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final StudentMapper studentMapper;
    private final OrganizerMapper organizerMapper;
    private final AdminMapper adminMapper;
    private final ActivityService activityService;

    public Map<String, Object> users(AuthUser user) {
        activityService.requireRole(user, "admin");
        return Map.of(
                "students", studentMapper.selectList(null),
                "organizers", organizerMapper.selectList(null),
                "admins", adminMapper.selectList(null));
    }

    public void updateStatus(String type, Long id, String status, AuthUser user) {
        activityService.requireRole(user, "admin");
        int rows = switch (type) {
            case "student" -> {
                var entity = studentMapper.selectById(id);
                if (entity == null) yield 0;
                entity.setAccountStatus(status);
                yield studentMapper.updateById(entity);
            }
            case "organizer" -> {
                var entity = organizerMapper.selectById(id);
                if (entity == null) yield 0;
                entity.setAccountStatus(status);
                yield organizerMapper.updateById(entity);
            }
            case "admin" -> {
                var entity = adminMapper.selectById(id);
                if (entity == null) yield 0;
                entity.setAccountStatus(status);
                yield adminMapper.updateById(entity);
            }
            default -> throw new BusinessException("不支持的用户类型");
        };
        if (rows == 0) {
            throw new BusinessException("用户不存在");
        }
    }
}
