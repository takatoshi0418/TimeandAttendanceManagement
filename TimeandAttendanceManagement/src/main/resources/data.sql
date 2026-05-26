-- 1. ロールの登録
INSERT INTO roles (authority, name) VALUES ('ROLE_ADMIN', '管理者');
INSERT INTO roles (authority, name) VALUES ('ROLE_STAFF', '一般社員');

-- 2. ユーザーの登録 (※passwordは 'admin123' をハッシュ化したもの)
INSERT INTO users (employee_number, password, last_name, first_name, role_id)
VALUES ('admin001', '$2a$10$qlXVUP9NeNnK0KOcCIuKrOE9.wLH9n2QC./XVekDbMrzLUXdmiGWO', 'システム', '管理者', 1);

INSERT INTO users (employee_number, password, last_name, first_name, role_id)
VALUES ('staff001', '$2a$10$qlXVUP9NeNnK0KOcCIuKrOE9.wLH9n2QC./XVekDbMrzLUXdmiGWO', '山田', '太郎', 2);

-- 3. 勤怠データの登録
INSERT INTO attendance (user_id, clock_in, clock_out)
VALUES (1, '2026-05-24 09:00:00', '2026-05-24 17:00:00');

INSERT INTO attendance (user_id, clock_in, clock_out)
VALUES (2, '2026-05-24 09:30:00', '2026-05-24 17:30:00');