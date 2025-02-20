-- ✅ 중복 방지를 위해 INSERT IGNORE 또는 REPLACE 사용
INSERT IGNORE INTO authority (authority_name) VALUES ('ROLE_ADMIN');

-- ✅ admin 계정이 없는 경우에만 추가
INSERT IGNORE INTO `user` (id, password, nickname, activated)
VALUES ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);

-- ✅ user_authority 테이블에 ROLE_ADMIN이 없을 경우만 추가
INSERT IGNORE INTO user_authority (user_id, authority_name)
VALUES ((SELECT user_id FROM `user` WHERE id='admin'), 'ROLE_ADMIN');

-- ✅ trip_data 테이블 생성 (없을 경우에만)
CREATE TABLE IF NOT EXISTS trip_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id VARCHAR(50),
    timestamp DATETIME NOT NULL, -- ✅ VARCHAR(255) → DATETIME 변경
    latitude DOUBLE,
    longitude DOUBLE,
    value DOUBLE DEFAULT 0.0 -- ✅ value 컬럼 추가 (기본값 0.0)
);

-- ✅ 기존 테이블 컬럼 수정 (VARCHAR → DATETIME)
ALTER TABLE trip_data
MODIFY COLUMN timestamp DATETIME NOT NULL;

-- ✅ 데이터 삽입 (value 필드 추가)
INSERT INTO trip_data (car_number, timestamp, latitude, longitude, value)
VALUES ('V1234', '2025-02-18 12:00:00', 37.5665, 126.9780, 0.0);

-- ✅ 통계 테이블 생성
CREATE TABLE IF NOT EXISTS statistics_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME NOT NULL,
    count BIGINT,
    avg_value DOUBLE
);

-- ✅ 데이터 테이블 생성
CREATE TABLE IF NOT EXISTS data_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME NOT NULL,
    value DOUBLE
);

-- ✅ 테스트용 데이터 삽입
INSERT INTO data_table (timestamp, value) VALUES ('2024-02-17 00:00:00', 10.5);
INSERT INTO data_table (timestamp, value) VALUES ('2024-02-17 00:05:00', 20.0);
INSERT INTO data_table (timestamp, value) VALUES ('2024-02-17 00:10:00', 30.3);
