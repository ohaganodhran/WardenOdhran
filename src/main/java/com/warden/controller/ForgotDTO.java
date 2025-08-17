package com.warden.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotDTO {
    private String newPassword;
    private String confirmPassword;

    public boolean passwordsMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
