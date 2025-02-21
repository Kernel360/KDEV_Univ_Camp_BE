-- ✅ 중복 방지를 위해 INSERT IGNORE 또는 REPLACE 사용
INSERT IGNORE INTO authority (authority_name) VALUES ('ROLE_ADMIN');

-- ✅ admin 계정이 없는 경우에만 추가
INSERT IGNORE INTO `user` (id, password, nickname, activated)
VALUES ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);

-- ✅ user_authority 테이블에 ROLE_ADMIN이 없을 경우만 추가
INSERT IGNORE INTO user_authority (user_id, authority_name)
VALUES ((SELECT id FROM `user` WHERE id='admin'), 'ROLE_ADMIN');

-- ✅ trip_data 테이블 생성 (없을 경우에만)
CREATE TABLE IF NOT EXISTS trip_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id VARCHAR(50) NOT NULL, -- ✅ 추가 (외래키 가능)
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    latitude DOUBLE,
    longitude DOUBLE,
    value DOUBLE DEFAULT 0.0
);

-- ✅ 기존 테이블 컬럼 수정 (VARCHAR → DATETIME, 기본값 추가)
ALTER TABLE trip_data
MODIFY COLUMN timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- ✅ 데이터 삽입 (컬럼명 vehicle_id로 수정)
INSERT INTO trip_data (vehicle_id, timestamp, latitude, longitude, value)
VALUES ('V1234', '2025-02-18 12:00:00', 37.5665, 126.9780, 0.0);

-- ✅ 통계 테이블 생성
CREATE TABLE IF NOT EXISTS statistics_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    count BIGINT,
    avg_value DOUBLE
);

-- ✅ 데이터 테이블 생성
CREATE TABLE IF NOT EXISTS data_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    value DOUBLE
);

-- ✅ 테스트용 데이터 삽입
INSERT INTO data_table (timestamp, value) VALUES ('2024-02-17 00:00:00', 10.5);
INSERT INTO data_table (timestamp, value) VALUES ('2024-02-17 00:05:00', 20.0);
INSERT INTO data_table (timestamp, value) VALUES ('2024-02-17 00:10:00', 30.3);

-- ✅ 차량 이벤트 테이블 생성 (시동 ON/OFF 저장)
CREATE TABLE IF NOT EXISTS vehicle_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL CHECK (event_type IN ('START', 'STOP')), -- ✅ 유효값 제한
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);