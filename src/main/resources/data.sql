-- 1️⃣ 권한 테이블에 데이터 삽입 (가장 먼저 실행)
INSERT INTO authority (authority_name) VALUES ('ROLE_USER');
INSERT INTO authority (authority_name) VALUES ('ROLE_ADMIN');

-- 2️⃣ 사용자 테이블에 데이터 삽입 (`user`는 예약어이므로 백틱 사용)
INSERT INTO `user` (username, password, nickname, activated) 
VALUES ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);

INSERT INTO `user` (username, password, nickname, activated) 
VALUES ('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'user', 1);

-- 3️⃣ 삽입된 사용자 ID 확인 (MySQL에서는 auto_increment가 보장되지 않음)
SELECT user_id, username FROM `user`;

-- 4️⃣ user_authority 테이블에 데이터 삽입 (`user_id` 값 확인 후 조정 필요)
INSERT INTO user_authority (user_id, authority_name) 
VALUES ((SELECT user_id FROM `user` WHERE username='admin'), 'ROLE_USER');

INSERT INTO user_authority (user_id, authority_name) 
VALUES ((SELECT user_id FROM `user` WHERE username='admin'), 'ROLE_ADMIN');

INSERT INTO user_authority (user_id, authority_name) 
VALUES ((SELECT user_id FROM `user` WHERE username='user'), 'ROLE_USER');
