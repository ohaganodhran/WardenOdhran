create table if not exists Credential
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    User_id BIGINT NOT NULL,
    site_name VARCHAR(255) NOT NULL,
    site_username VARCHAR(255),
    site_password_hashed VARCHAR(255),
    notes text,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,

    Constraint fk_user_id FOREIGN KEY (User_id) REFERENCES User(id) ON DELETE CASCADE
);