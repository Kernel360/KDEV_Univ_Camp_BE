-- 중복 방지를 위해 INSERT IGNORE 또는 REPLACE 사용
INSERT IGNORE INTO authority (authority_name) VALUES ('ROLE_ADMIN');

-- admin 계정이 없는 경우에만 추가
INSERT IGNORE INTO `user` (id, password, nickname, activated)
VALUES ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);

-- user_authority 테이블에 ROLE_ADMIN이 없을 경우만 추가
INSERT IGNORE INTO user_authority (user_id, authority_name)
VALUES ((SELECT user_id FROM `user` WHERE id='admin'), 'ROLE_ADMIN');

-- trip_data 테이블 생성 (없을 경우에만)
CREATE TABLE IF NOT EXISTS trip_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id VARCHAR(50),
    timestamp VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE
);

-- 기존 테이블 컬럼 수정 (기존 date, time 필드를 제거하고 timestamp 추가)
ALTER TABLE trip_data
DROP COLUMN IF EXISTS date,
DROP COLUMN IF EXISTS time,
ADD COLUMN IF NOT EXISTS timestamp VARCHAR(255);
