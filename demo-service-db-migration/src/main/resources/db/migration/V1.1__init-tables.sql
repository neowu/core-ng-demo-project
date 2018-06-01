CREATE TABLE IF NOT EXISTS `customer` (
  `id`           INT AUTO_INCREMENT,
  `status`       VARCHAR(10) NOT NULL,
  `email`        VARCHAR(50) NOT NULL,
  `first_name`   VARCHAR(50) NOT NULL,
  `last_name`    VARCHAR(50),
  `updated_time` DATETIME    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;
