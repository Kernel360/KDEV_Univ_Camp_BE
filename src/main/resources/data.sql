-- 중복 방지를 위해 INSERT IGNORE 또는 REPLACE 사용
INSERT IGNORE INTO authority (authority_name) VALUES ('ROLE_ADMIN');

-- admin 계정이 없는 경우에만 추가
INSERT IGNORE INTO `user` (username, password, nickname, activated)
VALUES ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);

-- user_authority 테이블에 ROLE_ADMIN이 없을 경우만 추가
INSERT IGNORE INTO user_authority (user_id, authority_name)
VALUES ((SELECT user_id FROM `user` WHERE username='admin'), 'ROLE_ADMIN');
