CREATE TABLE IF NOT EXISTS `roles` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(20) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS `users` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `uuid` varchar(36) NOT NULL UNIQUE,
    `first_name` varchar(50) NOT NULL,
    `last_name` varchar(50) NOT NULL,
    `email` varchar(50) NOT NULL UNIQUE,
    `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS `users_roles` (
    `user_id` int NOT NULL,
    `role_id` int NOT NULL,
    CONSTRAINT uc_user_role UNIQUE (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO roles (name) VALUE ('ROLE_ADMIN');
INSERT INTO roles (name) VALUE ('ROLE_USER');
INSERT INTO users (uuid, first_name, last_name, email, password) VALUES ('00000000-0000-0000-0000-000000000000', 'root', 'root', 'root@miriad.truesoft.com', '$2a$10$mfDYZ8xRF10J9tm.byWUBuXciCMp8DzfBmYirrLXbQcqrehjTrVBa');
INSERT INTO users_roles (user_id, role_id) VALUE (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUE (1, 2);