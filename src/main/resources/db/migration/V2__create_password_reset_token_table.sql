create table if not EXISTS password_reset_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token_hash VARCHAR(255) NOT NULL,
    User_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_user_reset FOREIGN KEY (User_id) REFERENCES User(id) ON DELETE CASCADE
);
