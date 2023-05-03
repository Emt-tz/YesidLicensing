CREATE TABLE user (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);


CREATE TABLE auth_token (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT,
  token VARCHAR(255),
  expires TIMESTAMP
);

CREATE TABLE license (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  platform VARCHAR(255) NOT NULL,
  sdk VARCHAR(255) NOT NULL,
  service_type VARCHAR(255) NOT NULL,
  license_key VARCHAR(255) NOT NULL,
  max_transactions INT NOT NULL,
  expiration_date TIMESTAMP NOT NULL,
  creation_date TIMESTAMP NOT NULL
);

CREATE TABLE license_transaction (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  license_id BIGINT NOT NULL,
  license_key VARCHAR(255) NOT NULL,
  platform VARCHAR(255) NOT NULL,
  sdk VARCHAR(255) NOT NULL,
  service_type VARCHAR(255) NOT NULL,
  transaction_amount INT NOT NULL,
  transaction_date TIMESTAMP NOT NULL
);
