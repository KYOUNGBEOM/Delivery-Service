CREATE TABLE IF NOT EXISTS `mydb`.`store` (
  `id` BIGINT(32) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `address` VARCHAR(150) NOT NULL,
  `status` VARCHAR(50) NULL,
  `category` VARCHAR(50) NULL,
  `star` DOUBLE NULL,
  `thumbnail_url` VARCHAR(200) NULL,
  `minimum_amount` DECIMAL(11,4) NULL,
  `minimum_delivery_amount` DECIMAL(11,4) NULL,
  `phone_number` VARCHAR(20) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `thumbnail_url_UNIQUE` (`thumbnail_url` ASC) VISIBLE,
  UNIQUE INDEX `minimum_amount_UNIQUE` (`minimum_amount` ASC) VISIBLE,
  UNIQUE INDEX `minimum_delivery_amount_UNIQUE` (`minimum_delivery_amount` ASC) VISIBLE)
ENGINE = InnoDB