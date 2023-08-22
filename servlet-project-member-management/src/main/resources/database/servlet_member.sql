use servlet_member;

CREATE TABLE servlet_member.`member`
(
    `id`         VARCHAR(30)  NOT NULL,
    `password`   VARCHAR(16)  NOT NULL,
    `name`       VARCHAR(20)  NOT NULL,
    `gender`     VARCHAR(4)   NULL     DEFAULT NULL,
    `birth`      VARCHAR(10)  NULL     DEFAULT NULL,
    `email`      VARCHAR(50)  NULL     DEFAULT NULL,
    `phone`      VARCHAR(13)  NULL     DEFAULT NULL,
    `zipcode`    VARCHAR(5)   NULL     DEFAULT NULL,
    `addr1`      VARCHAR(100) NULL     DEFAULT NULL,
    `addr2`      VARCHAR(100) NULL     DEFAULT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT now(),
    `updated_at` DATETIME     NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
);

CREATE TABLE servlet_member.`board`
(
    `num`         bigint       NOT NULL AUTO_INCREMENT,
    `member_id`   VARCHAR(10)  NOT NULL,
    `member_name` VARCHAR(10)  NOT NULL,
    `title`       VARCHAR(100) NOT NULL,
    `content`     TEXT         NOT NULL,
    `hit`         int,
    `ip`          VARCHAR(20),
    `created_at`  DATETIME     NOT NULL DEFAULT now(),
    `updated_at`  DATETIME     NULL     DEFAULT NULL,
    PRIMARY KEY (`num`)
) DEFAULT CHARSET = utf8;

CREATE TABLE servlet_member.`ripple`
(
    `rippleId`    bigint      NOT NULL AUTO_INCREMENT,
    `board_num`   bigint      NOT NULL,
    `member_id`   VARCHAR(10) NOT NULL,
    `member_name` VARCHAR(10) NOT NULL,
    `content`     TEXT        NOT NULL,
    `ip`          VARCHAR(20),
    `created_at`  DATETIME    NOT NULL DEFAULT now(),
    `updated_at`  DATETIME    NULL     DEFAULT NULL,
    PRIMARY KEY (`rippleId`)
) DEFAULT CHARSET = utf8;