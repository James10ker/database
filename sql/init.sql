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
('2026001', '123456', N'Wang Xize', N'Computer College', N'Software Engineering', N'2023', '13800000001', '2026001@example.com', N'lecture,competition'),
('2026002', '123456', N'Lan Ying', N'Management College', N'Information Management', N'2023', '13800000002', '2026002@example.com', N'volunteer,art'),
('2026003', '123456', N'Tang Yuhan', N'Art College', N'Visual Design', N'2024', '13800000003', '2026003@example.com', N'art,lecture'),
('2026004', '123456', N'He Jiaqian', N'Foreign Language College', N'English', N'2024', '13800000004', '2026004@example.com', N'volunteer,sports'),
('2026005', '123456', N'Chen Student', N'Computer College', N'Data Science', N'2025', '13800000005', '2026005@example.com', N'competition,sports');

INSERT INTO organizer (organizer_no, password, organizer_name, organizer_type, contact_name, contact_phone) VALUES
('org001', '123456', N'Student Union', N'University Organization', N'Teacher Li', '13900000001'),
('org002', '123456', N'Computer Association', N'Club', N'Student Zhou', '13900000002');

INSERT INTO admin_user (username, password, admin_name) VALUES
('admin', '123456', N'System Admin');

INSERT INTO activity (activity_title, category, description, organizer_id, location, start_time, end_time, register_start_time, register_end_time, capacity, activity_status, audit_status, checkin_code) VALUES
(N'Campus Tech Innovation Lecture', N'lecture', N'Engineers share AI application practice.', 2, N'Library Hall',
 DATEADD(DAY, 5, SYSDATETIME()), DATEADD(HOUR, 2, DATEADD(DAY, 5, SYSDATETIME())),
 DATEADD(DAY, -1, SYSDATETIME()), DATEADD(DAY, 4, SYSDATETIME()), 80, 'published', 'approved', NULL),
(N'Spring Volunteer Service', N'volunteer', N'Volunteer service around campus.', 1, N'East Gate',
 DATEADD(DAY, 7, SYSDATETIME()), DATEADD(HOUR, 3, DATEADD(DAY, 7, SYSDATETIME())),
 DATEADD(DAY, -1, SYSDATETIME()), DATEADD(DAY, 6, SYSDATETIME()), 50, 'published', 'approved', NULL),
(N'Basketball Friendly Match', N'sports', N'Basketball match between colleges.', 1, N'Gym',
 DATEADD(DAY, 10, SYSDATETIME()), DATEADD(HOUR, 2, DATEADD(DAY, 10, SYSDATETIME())),
 DATEADD(DAY, -1, SYSDATETIME()), DATEADD(DAY, 9, SYSDATETIME()), 30, 'published', 'approved', NULL),
(N'Database Design Competition', N'competition', N'Database modeling competition for course design.', 2, N'Lab A305',
 DATEADD(DAY, 12, SYSDATETIME()), DATEADD(HOUR, 4, DATEADD(DAY, 12, SYSDATETIME())),
 DATEADD(DAY, -1, SYSDATETIME()), DATEADD(DAY, 11, SYSDATETIME()), 40, 'published', 'approved', NULL),
(N'Graduation Music Concert', N'art', N'Graduation season performance for the whole school.', 1, N'Auditorium',
 DATEADD(DAY, -2, SYSDATETIME()), DATEADD(HOUR, 2, DATEADD(DAY, -2, SYSDATETIME())),
 DATEADD(DAY, -10, SYSDATETIME()), DATEADD(DAY, -3, SYSDATETIME()), 200, 'ended', 'approved', '888888');

INSERT INTO registration (activity_id, student_id, registration_time, registration_status) VALUES
(1, 1, DATEADD(HOUR, -3, SYSDATETIME()), 'registered'),
(2, 2, DATEADD(HOUR, -2, SYSDATETIME()), 'registered'),
(5, 1, DATEADD(DAY, -5, SYSDATETIME()), 'registered'),
(5, 3, DATEADD(DAY, -5, SYSDATETIME()), 'registered');

INSERT INTO checkin (registration_id, activity_id, student_id, checkin_time, checkin_method, checkin_status, remark) VALUES
(3, 5, 1, DATEADD(DAY, -2, SYSDATETIME()), 'code', 'checked_in', NULL),
(4, 5, 3, DATEADD(DAY, -2, SYSDATETIME()), 'manual', 'manual_checked_in', N'Manual checkin from onsite list');

INSERT INTO rating (activity_id, student_id, checkin_id, score, comment, rating_time) VALUES
(5, 1, 1, 5, N'Well organized and useful.', DATEADD(DAY, -1, SYSDATETIME())),
(5, 3, 2, 4, N'Good event. Seat guidance can be clearer.', DATEADD(DAY, -1, SYSDATETIME()));
GO
