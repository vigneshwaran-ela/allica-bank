CREATE TABLE IF NOT EXISTS retailer (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL UNIQUE,
    api_key VARCHAR(255) NOT NULL
);

-- Set auto-increment starting value
--ALTER TABLE retailer ALTER COLUMN id RESTART WITH 100;

CREATE TABLE IF NOT EXISTS customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_name VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    dob VARCHAR(50),
    retailer_id BIGINT,
    CONSTRAINT fk_customer_retailer FOREIGN KEY (retailer_id) REFERENCES retailer(id)
);

-- Set auto-increment starting value
ALTER TABLE customer ALTER COLUMN id RESTART WITH 100;

CREATE TABLE IF NOT EXISTS admin_login (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);