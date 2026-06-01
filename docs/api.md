# 校园活动管理系统接口文档

## 通用说明

- 基础地址：`http://localhost:8080/api`
- 认证方式：登录后将返回的 `token` 放入请求头 `Authorization`。
- 统一返回：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 测试账号

| 角色 | 账号 | 密码 |
|---|---|---|
| 学生 | `2026001` | `123456` |
| 组织者 | `org001` | `123456` |
| 管理员 | `admin` | `123456` |

## 认证与用户

| 方法 | 路径 | 说明 |
|---|---|---|
| `POST` | `/login` | 登录，参数：`username`、`password`、`role` |
| `GET` | `/users/me` | 当前登录用户 |
| `GET` | `/admin/users` | 管理员查看用户列表 |
| `PUT` | `/admin/users/{type}/{id}/status` | 管理员修改账号状态，`type=student/organizer/admin` |

## 活动

| 方法 | 路径 | 说明 |
|---|---|---|
| `GET` | `/activities` | 活动列表，支持 `keyword/category/status/organizerId` |
| `GET` | `/activities/{id}` | 活动详情 |
| `POST` | `/activities` | 组织者创建活动 |
| `PUT` | `/activities/{id}` | 组织者修改自己的活动 |
| `PUT` | `/activities/{id}/publish` | 组织者发布活动 |
| `PUT` | `/activities/{id}/cancel` | 组织者取消活动 |
| `PUT` | `/admin/activities/{id}/audit` | 管理员审核活动 |

## 报名

| 方法 | 路径 | 说明 |
|---|---|---|
| `POST` | `/registrations` | 学生报名，参数：`activityId` |
| `PUT` | `/registrations/{id}/cancel` | 学生取消报名 |
| `GET` | `/registrations/mine` | 学生查看自己的报名 |
| `GET` | `/activities/{id}/registrations` | 组织者查看活动报名名单 |

## 签到、评分、统计、推荐

| 方法 | 路径 | 说明 |
|---|---|---|
| `POST` | `/checkins/code` | 组织者生成签到码 |
| `POST` | `/checkins` | 学生签到，参数：`activityId`、`checkinCode` |
| `POST` | `/checkins/manual` | 组织者人工补签，参数：`registrationId`、`remark` |
| `POST` | `/ratings` | 学生评分，参数：`activityId`、`score`、`comment` |
| `GET` | `/statistics/activity/{id}` | 活动统计 |
| `GET` | `/recommendations` | 学生推荐活动 |

## 本地启动

1. 在 SSMS 或 `sqlcmd` 中执行 `sql/init.sql`。
2. 根据本机 SQL Server 认证方式调整 `backend/src/main/resources/application.yml` 中的数据库账号密码。
3. 后端：在 `backend` 目录运行 `mvn spring-boot:run`。
4. 前端：在 `frontend` 目录运行 `npm install`，再运行 `npm run dev`。
