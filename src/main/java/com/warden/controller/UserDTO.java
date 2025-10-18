package com.warden.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private int id;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[^a-zA-Z]).{8,}$",
            message = "Password must have at least 1 uppercase letter and 1 special or numeric character"
    )
    private String password;

    @NotNull(message = "Validate your password")
    private String confirmPassword;

    @Email(message = "Email must be in valid format")
    @NotNull(message = "Email is required")
    private String email;

    @NotNull(message = "Username is required")
    private String username;

    public boolean passwordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
