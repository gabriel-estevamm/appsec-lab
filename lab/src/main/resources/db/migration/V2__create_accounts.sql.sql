CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0.00,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uq_account_user UNIQUE (user_id)
);
