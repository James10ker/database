# 校园活动管理系统

这是根据 `docs/` 下需求分析、概念逻辑结构设计和开发方案搭建的可运行系统骨架，包含：

- `backend/`：Spring Boot 3 + MyBatis-Plus 后端。
- `frontend/`：Vue 3 + Vite + Element Plus 前端。
- `sql/init.sql`：SQL Server 建库、建表、约束和测试数据。
- `docs/api.md`：接口说明和测试账号。

## 快速启动

1. 执行数据库脚本：

```powershell
sqlcmd -S .\SQLEXPRESS -E -i .\sql\init.sql
```

2. 根据本机 SQL Server 登录方式配置后端连接。

```powershell
$env:DB_URL='jdbc:sqlserver://localhost\SQLEXPRESS;databaseName=campus_activity;encrypt=true;trustServerCertificate=true'
$env:DB_USERNAME='sa'
$env:DB_PASSWORD='YourStrong!Passw0rd'
```

3. 启动后端：

```powershell
cd backend
mvn spring-boot:run
```

4. 启动前端：

```powershell
cd frontend
npm install
npm run dev
```

## 测试账号

| 角色 | 账号 | 密码 |
|---|---|---|
| 学生 | `2026001` | `123456` |
| 组织者 | `org001` | `123456` |
| 管理员 | `admin` | `123456` |
