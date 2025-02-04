INSERT INTO authority (authority_name) VALUES ('ROLE_ADMIN');

INSERT INTO `user` (username, password, nickname, activated)
VALUES ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);

INSERT INTO user_authority (user_id, authority_name)
VALUES ((SELECT user_id FROM `user` WHERE username='admin'), 'ROLE_ADMIN');
