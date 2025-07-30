CREATE TABLE `users` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `email` varchar(255) DEFAULT NULL,
                         `phone_number` varchar(255) DEFAULT NULL,
                         `user_name` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `admins` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `admin_name` varchar(255) DEFAULT NULL,
                          `email` varchar(255) DEFAULT NULL,
                          `phone_number` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `new_snakes` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `average_length` double NOT NULL,
                              `color` varchar(255) DEFAULT NULL,
                              `name` varchar(255) DEFAULT NULL,
                              `pattern` varchar(255) DEFAULT NULL,
                              `species` varchar(255) DEFAULT NULL,
                              `status` enum('APPROVED','PENDING','REJECTED') DEFAULT NULL,
                              `venomous` enum('DEADLY','HIGHLY_VENOMOUS','MILDLY_VENOMOUS','NON_VENOMOUS') DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `blog_image_url` (
                                  `blog_id` bigint(20) NOT NULL,
                                  `image_url` varchar(255) DEFAULT NULL,
                                  KEY `FKenjk9fa83d05lwco8d87efe1c` (`blog_id`),
                                  CONSTRAINT `FKenjk9fa83d05lwco8d87efe1c` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `blogs` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `content` varchar(255) DEFAULT NULL,
                         `title` varchar(255) DEFAULT NULL,
                         `user_id` bigint(20) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FKpg4damav6db6a6fh5peylcni5` (`user_id`),
                         CONSTRAINT `FKpg4damav6db6a6fh5peylcni5` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `requested_new_snake_image_url` (
                                                 `requested_new_snake_id` bigint(20) NOT NULL,
                                                 `image_url` varchar(255) DEFAULT NULL,
                                                 KEY `FKraupgwwroun8knwfvrsvi8tm6` (`requested_new_snake_id`),
                                                 CONSTRAINT `FKraupgwwroun8knwfvrsvi8tm6` FOREIGN KEY (`requested_new_snake_id`) REFERENCES `new_snakes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `snake_image_url` (
                                   `snake_id` bigint(20) NOT NULL,
                                   `image_url` varchar(255) DEFAULT NULL,
                                   KEY `FK5ecu96a5pbbgw2dgmgns6r1r8` (`snake_id`),
                                   CONSTRAINT `FK5ecu96a5pbbgw2dgmgns6r1r8` FOREIGN KEY (`snake_id`) REFERENCES `snakes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `snakes` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                          `average_length` double NOT NULL,
                          `color` varchar(255) DEFAULT NULL,
                          `name` varchar(255) DEFAULT NULL,
                          `pattern` varchar(255) DEFAULT NULL,
                          `species` varchar(255) DEFAULT NULL,
                          `venomous` enum('DEADLY','HIGHLY_VENOMOUS','MILDLY_VENOMOUS','NON_VENOMOUS') DEFAULT NULL,
                          `admin_id` bigint(20) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKqo0f9qyrfto9dd3wadqhmalp5` (`admin_id`),
                          CONSTRAINT `FKqo0f9qyrfto9dd3wadqhmalp5` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `symptoms` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `description` varchar(255) DEFAULT NULL,
                            `name` varchar(255) DEFAULT NULL,
                            `admin_id` bigint(20) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FK2sb032h5anoxjo7dmsj5ucu28` (`admin_id`),
                            CONSTRAINT `FK2sb032h5anoxjo7dmsj5ucu28` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `admins_first_aids` (
                                     `admin_id` bigint(20) NOT NULL,
                                     `first_aids_id` bigint(20) NOT NULL,
                                     UNIQUE KEY `UKiiqw5c7chg2ub6hag48ul15em` (`first_aids_id`),
                                     KEY `FKs79x962w9w78694d53mtcbl8w` (`admin_id`),
                                     CONSTRAINT `FKkha2fotlxc7guvdhrqxhulltn` FOREIGN KEY (`first_aids_id`) REFERENCES `first_aids` (`id`),
                                     CONSTRAINT `FKs79x962w9w78694d53mtcbl8w` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `admins_symptoms` (
                                   `admin_id` bigint(20) NOT NULL,
                                   `symptoms_id` bigint(20) NOT NULL,
                                   UNIQUE KEY `UKbvgbr7jk2ms9aiix3qsebdic6` (`symptoms_id`),
                                   KEY `FKhqwrkcvrgbf4m6e6dlygfpwli` (`admin_id`),
                                   CONSTRAINT `FKbbiqgx179bq4tdmc5u0i56lsi` FOREIGN KEY (`symptoms_id`) REFERENCES `symptoms` (`id`),
                                   CONSTRAINT `FKhqwrkcvrgbf4m6e6dlygfpwli` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `comments` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT,
                            `comment_text` varchar(255) DEFAULT NULL,
                            `blog_id` bigint(20) DEFAULT NULL,
                            `user_id` bigint(20) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FK9aakob3a7aghrm94k9kmbrjqd` (`blog_id`),
                            KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
                            CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                            CONSTRAINT `FK9aakob3a7aghrm94k9kmbrjqd` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `first_aids` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `description` varchar(255) DEFAULT NULL,
                              `name` varchar(255) DEFAULT NULL,
                              `admin_id` bigint(20) DEFAULT NULL,
                              `symptom_id` bigint(20) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `FKsdgbecivs51qj528ocniakq5t` (`admin_id`),
                              KEY `FKmimfaog9oywovhwwijnbgukx7` (`symptom_id`),
                              CONSTRAINT `FKmimfaog9oywovhwwijnbgukx7` FOREIGN KEY (`symptom_id`) REFERENCES `symptoms` (`id`),
                              CONSTRAINT `FKsdgbecivs51qj528ocniakq5t` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `replies` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `reply_body` varchar(255) DEFAULT NULL,
                           `comment_id` bigint(20) DEFAULT NULL,
                           `user_id` bigint(20) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKn0xus92n25hvud6dlfni0ttqg` (`comment_id`),
                           KEY `FKn60t7po8l0rllye52xx25q4xx` (`user_id`),
                           CONSTRAINT `FKn0xus92n25hvud6dlfni0ttqg` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`),
                           CONSTRAINT `FKn60t7po8l0rllye52xx25q4xx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

CREATE TABLE `snake_symptoms` (
                                  `symptom_id` bigint(20) NOT NULL,
                                  `snake_id` bigint(20) NOT NULL,
                                  KEY `FKjjcwemcgg21jsec9018spcma2` (`snake_id`),
                                  KEY `FKth8brufxlha6b8f9xqy5yxo0e` (`symptom_id`),
                                  CONSTRAINT `FKjjcwemcgg21jsec9018spcma2` FOREIGN KEY (`snake_id`) REFERENCES `snakes` (`id`),
                                  CONSTRAINT `FKth8brufxlha6b8f9xqy5yxo0e` FOREIGN KEY (`symptom_id`) REFERENCES `symptoms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci ;

