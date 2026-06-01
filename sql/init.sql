IF DB_ID(N'campus_activity') IS NULL
BEGIN
    CREATE DATABASE campus_activity;
END
GO

USE campus_activity;
GO

IF OBJECT_ID(N'rating', N'U') IS NOT NULL DROP TABLE rating;
IF OBJECT_ID(N'checkin', N'U') IS NOT NULL DROP TABLE checkin;
IF OBJECT_ID(N'registration', N'U') IS NOT NULL DROP TABLE registration;
IF OBJECT_ID(N'activity', N'U') IS NOT NULL DROP TABLE activity;
IF OBJECT_ID(N'admin_user', N'U') IS NOT NULL DROP TABLE admin_user;
IF OBJECT_ID(N'organizer', N'U') IS NOT NULL DROP TABLE organizer;
IF OBJECT_ID(N'student', N'U') IS NOT NULL DROP TABLE student;
GO

CREATE TABLE student (
    student_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    student_no VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    student_name NVARCHAR(50) NOT NULL,
    college NVARCHAR(100),
    major NVARCHAR(100),
    grade NVARCHAR(20),
    phone VARCHAR(30),
    email VARCHAR(100),
    interest_tags NVARCHAR(200),
    account_status VARCHAR(20) NOT NULL DEFAULT 'enabled',
    created_time DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE organizer (
    organizer_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    organizer_no VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    organizer_name NVARCHAR(100) NOT NULL,
    organizer_type NVARCHAR(50) NOT NULL,
    contact_name NVARCHAR(50),
    contact_phone VARCHAR(30),
    account_status VARCHAR(20) NOT NULL DEFAULT 'enabled',
    created_time DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE admin_user (
    admin_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    admin_name NVARCHAR(50) NOT NULL,
    account_status VARCHAR(20) NOT NULL DEFAULT 'enabled',
    created_time DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE activity (
    activity_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    activity_title NVARCHAR(100) NOT NULL,
    category NVARCHAR(50) NOT NULL,
    description NVARCHAR(1000),
    organizer_id BIGINT NOT NULL,
    location NVARCHAR(100) NOT NULL,
    start_time DATETIME2 NOT NULL,
    end_time DATETIME2 NOT NULL,
    register_start_time DATETIME2 NOT NULL,
    register_end_time DATETIME2 NOT NULL,
    capacity INT NOT NULL,
    activity_status VARCHAR(20) NOT NULL DEFAULT 'draft',
    audit_status VARCHAR(20) NOT NULL DEFAULT 'pending',
    checkin_code VARCHAR(20),
    created_time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    updated_time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT fk_activity_organizer FOREIGN KEY (organizer_id) REFERENCES organizer(organizer_id),
    CONSTRAINT ck_activity_time CHECK (end_time > start_time),
    CONSTRAINT ck_activity_register_time CHECK (register_end_time <= start_time),
    CONSTRAINT ck_activity_capacity CHECK (capacity > 0)
);

CREATE TABLE registration (
    registration_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    activity_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    registration_time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    registration_status VARCHAR(20) NOT NULL DEFAULT 'registered',
    cancel_time DATETIME2 NULL,
    CONSTRAINT fk_registration_activity FOREIGN KEY (activity_id) REFERENCES activity(activity_id),
    CONSTRAINT fk_registration_student FOREIGN KEY (student_id) REFERENCES student(student_id),
    CONSTRAINT uk_registration_student_activity UNIQUE (student_id, activity_id)
);

CREATE TABLE checkin (
    checkin_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    registration_id BIGINT NOT NULL UNIQUE,
    activity_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    checkin_time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    checkin_method VARCHAR(20) NOT NULL,
    checkin_status VARCHAR(20) NOT NULL,
    remark NVARCHAR(300),
    CONSTRAINT fk_checkin_registration FOREIGN KEY (registration_id) REFERENCES registration(registration_id),
    CONSTRAINT fk_checkin_activity FOREIGN KEY (activity_id) REFERENCES activity(activity_id),
    CONSTRAINT fk_checkin_student FOREIGN KEY (student_id) REFERENCES student(student_id)
);

CREATE TABLE rating (
    rating_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    activity_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    checkin_id BIGINT NOT NULL UNIQUE,
    score TINYINT NOT NULL,
    comment NVARCHAR(500),
    rating_time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT fk_rating_activity FOREIGN KEY (activity_id) REFERENCES activity(activity_id),
    CONSTRAINT fk_rating_student FOREIGN KEY (student_id) REFERENCES student(student_id),
    CONSTRAINT fk_rating_checkin FOREIGN KEY (checkin_id) REFERENCES checkin(checkin_id),
    CONSTRAINT uk_rating_student_activity UNIQUE (student_id, activity_id),
    CONSTRAINT ck_rating_score CHECK (score BETWEEN 1 AND 5)
);
GO

INSERT INTO student (student_no, password, student_name, college, major, grade, phone, email, interest_tags) VALUES
('2026001', '123456', N'王熙泽', N'计算机学院', N'软件工程', N'2023', '13800000001', '2026001@example.com', N'讲座,竞赛'),
('2026002', '123456', N'蓝莹', N'管理学院', N'信息管理', N'2023', '13800000002', '2026002@example.com', N'志愿,文艺'),
('2026003', '123456', N'唐瑀含', N'艺术学院', N'视觉传达', N'2024', '13800000003', '2026003@example.com', N'文艺,讲座'),
('2026004', '123456', N'何佳倩', N'外国语学院', N'英语', N'2024', '13800000004', '2026004@example.com', N'志愿,体育'),
('2026005', '123456', N'陈同学', N'计算机学院', N'数据科学', N'2025', '13800000005', '2026005@example.com', N'竞赛,体育');

INSERT INTO organizer (organizer_no, password, organizer_name, organizer_type, contact_name, contact_phone) VALUES
('org001', '123456', N'校学生会', N'校级组织', N'李老师', '13900000001'),
('org002', '123456', N'计算机协会', N'社团', N'周同学', '13900000002');

INSERT INTO admin_user (username, password, admin_name) VALUES
('admin', '123456', N'系统管理员');

INSERT INTO activity (activity_title, category, description, organizer_id, location, start_time, end_time, register_start_time, register_end_time, capacity, activity_status, audit_status, checkin_code) VALUES
(N'校园科技创新讲座', N'讲座', N'邀请企业工程师分享 AI 应用实践。', 2, N'图书馆报告厅',
 DATEADD(DAY, 5, SYSDATETIME()), DATEADD(HOUR, 2, DATEADD(DAY, 5, SYSDATETIME())),
 DATEADD(DAY, -1, SYSDATETIME()), DATEADD(DAY, 4, SYSDATETIME()), 80, 'published', 'approved', NULL),
(N'春季志愿服务行动', N'志愿', N'校园周边志愿清洁与服务活动。', 1, N'学校东门',
 DATEADD(DAY, 7, SYSDATETIME()), DATEADD(HOUR, 3, DATEADD(DAY, 7, SYSDATETIME())),
 DATEADD(DAY, -1, SYSDATETIME()), DATEADD(DAY, 6, SYSDATETIME()), 50, 'published', 'approved', NULL),
(N'篮球友谊赛', N'体育', N'学院间篮球友谊赛。', 1, N'体育馆',
 DATEADD(DAY, 10, SYSDATETIME()), DATEADD(HOUR, 2, DATEADD(DAY, 10, SYSDATETIME())),
 DATEADD(DAY, -1, SYSDATETIME()), DATEADD(DAY, 9, SYSDATETIME()), 30, 'published', 'approved', NULL),
(N'数据库设计竞赛', N'竞赛', N'围绕课程设计进行数据库建模竞赛。', 2, N'实验楼 A305',
 DATEADD(DAY, 12, SYSDATETIME()), DATEADD(HOUR, 4, DATEADD(DAY, 12, SYSDATETIME())),
 DATEADD(DAY, -1, SYSDATETIME()), DATEADD(DAY, 11, SYSDATETIME()), 40, 'published', 'approved', NULL),
(N'毕业季音乐会', N'文艺', N'面向全校的毕业季文艺演出。', 1, N'大礼堂',
 DATEADD(DAY, -2, SYSDATETIME()), DATEADD(HOUR, 2, DATEADD(DAY, -2, SYSDATETIME())),
 DATEADD(DAY, -10, SYSDATETIME()), DATEADD(DAY, -3, SYSDATETIME()), 200, 'ended', 'approved', '888888');

INSERT INTO registration (activity_id, student_id, registration_time, registration_status) VALUES
(1, 1, DATEADD(HOUR, -3, SYSDATETIME()), 'registered'),
(2, 2, DATEADD(HOUR, -2, SYSDATETIME()), 'registered'),
(5, 1, DATEADD(DAY, -5, SYSDATETIME()), 'registered'),
(5, 3, DATEADD(DAY, -5, SYSDATETIME()), 'registered');

INSERT INTO checkin (registration_id, activity_id, student_id, checkin_time, checkin_method, checkin_status, remark) VALUES
(3, 5, 1, DATEADD(DAY, -2, SYSDATETIME()), 'code', 'checked_in', NULL),
(4, 5, 3, DATEADD(DAY, -2, SYSDATETIME()), 'manual', 'manual_checked_in', N'现场名单补签');

INSERT INTO rating (activity_id, student_id, checkin_id, score, comment, rating_time) VALUES
(5, 1, 1, 5, N'活动组织很好，现场效果不错。', DATEADD(DAY, -1, SYSDATETIME())),
(5, 3, 2, 4, N'节目丰富，希望下次座位引导更清晰。', DATEADD(DAY, -1, SYSDATETIME()));
GO
