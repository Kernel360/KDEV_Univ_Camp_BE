-- 기본 사용자 삽입
INSERT IGNORE INTO user (username, password, nickname, activated) 
VALUES 
('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1),
('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 1);

-- 권한 추가 (중복 삽입 방지)
INSERT IGNORE INTO authority (authority_name) VALUES ('ROLE_USER');
INSERT IGNORE INTO authority (authority_name) VALUES ('ROLE_ADMIN');

-- 사용자 권한 매핑
INSERT IGNORE INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT IGNORE INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO user_authority (user_id, authority_name) VALUES (2, 'ROLE_USER');
