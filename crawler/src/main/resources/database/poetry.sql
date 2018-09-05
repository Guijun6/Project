DROP TABLE IF EXISTS `t_poetry_info`;

CREATE TABLE `t_poetry_info` (

  `meta_id`        VARCHAR(64) PRIMARY KEY,
  `meta_url`       VARCHAR(1024) NOT NULL,
  `meta_create`    DATE          NOT NULL,
  `author_name`    VARCHAR(64)   NOT NULL,
  `author_dynasty` VARCHAR(32)   NOT NULL,
  `content_title`  VARCHAR(512)  NOT NULL,
  `content_body`   TEXT
);