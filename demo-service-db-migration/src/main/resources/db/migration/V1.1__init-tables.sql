CREATE TABLE IF NOT EXISTS customer
(
    id           INT AUTO_INCREMENT,
    status       ENUM ('ACTIVE', 'INACTIVE') NOT NULL,
    email        VARCHAR(50)                 NOT NULL UNIQUE,
    name         VARCHAR(50),
    updated_time DATETIME                    NOT NULL,
    PRIMARY KEY (id)
);
