package com.warden.controller;

import com.warden.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialDTO {
    private Long id;
    private User user;
    private String siteName;
    private String username;
    private String passwordHash;
    private String notes;
}
