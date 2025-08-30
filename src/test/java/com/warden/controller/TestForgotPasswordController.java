package com.warden.controller;

import com.warden.entity.PasswordResetToken;
import com.warden.entity.User;
import com.warden.service.PasswordResetService;
import com.warden.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForgotPasswordController.class)
class TestForgotPasswordController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PasswordResetService passwordResetService;

    private User mockUser;
    private PasswordResetToken mockToken;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setEmail("odhran@example.com");
        mockUser.setUsername("odhran");

        mockToken = new PasswordResetToken();
        mockToken.setUser(mockUser);
        mockToken.setUsed(false);
        mockToken.setTokenHash("hashedToken");
        mockToken.setCreatedAt(LocalDateTime.now());
        mockToken.setExpiresAt(LocalDateTime.now().plusHours(1));
    }

    @Test
    void testOnLoad() throws Exception {
        mockMvc.perform(get("/forgotpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgotPassword"))
                .andExpect(model().attributeExists("forgot"));
    }

    @Test
    void testForgotPassword_Success() throws Exception {
        when(userService.findByEmail("odhran@example.com")).thenReturn(Optional.of(mockUser));
        doNothing().when(passwordResetService).sendResetEmail(mockUser.getEmail());

        mockMvc.perform(post("/forgotpassword")
                        .param("email", "odhran@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgotpassword/sent"));
    }

    @Test
    void testForgotPassword_NoEmail() throws Exception {
        mockMvc.perform(post("/forgotpassword")
                        .param("email", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgotpassword"))
                .andExpect(flash().attributeExists("forgotPasswordError"));
    }

    @Test
    void testForgotPassword_InvalidEmail() throws Exception {
        when(userService.findByEmail("wrong@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/forgotpassword")
                        .param("email", "wrong@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgotpassword/sent"))
                .andExpect(flash().attributeCount(0));
    }

    @Test
    void testShowResetForm_ValidToken() throws Exception {
        when(passwordResetService.verifyRawToken("validToken"))
                .thenReturn(Optional.of(mockToken));

        mockMvc.perform(get("/forgotpassword/reset")
                        .param("token", "validToken"))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPassword"))
                .andExpect(model().attributeExists("token"))
                .andExpect(model().attributeExists("forgotDTO"));
    }

    @Test
    void testShowResetForm_InvalidToken() throws Exception {
        when(passwordResetService.verifyRawToken("invalidToken"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/forgotpassword/reset")
                        .param("token", "invalidToken"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgotPassword"))
                .andExpect(flash().attributeExists("forgotPasswordError"));
    }

    @Test
    void testResetPassword_Success() throws Exception {
        when(passwordResetService.verifyRawToken("validToken"))
                .thenReturn(Optional.of(mockToken));
        doNothing().when(passwordResetService).resetPassword(mockToken, "NewPass123!");
        doNothing().when(passwordResetService).markTokenUsed(mockToken);

        mockMvc.perform(post("/forgotpassword/reset")
                        .param("token", "validToken")
                        .param("newPassword", "NewPass123!")
                        .param("confirmPassword", "NewPass123!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgotpassword/resetSuccess"));
    }

    @Test
    void testResetPassword_PasswordsDontMatch() throws Exception {
        mockMvc.perform(post("/forgotpassword/reset")
                        .param("token", "validToken")
                        .param("newPassword", "NewPass123!")
                        .param("confirmPassword", "WrongPass!"))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPassword"))
                .andExpect(model().attributeExists("token"))
                .andExpect(model().attributeExists("forgot"));
    }

    @Test
    void testResetPassword_InvalidToken() throws Exception {
        when(passwordResetService.verifyRawToken("invalidToken"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/forgotpassword/reset")
                        .param("token", "invalidToken")
                        .param("newPassword", "NewPass123!")
                        .param("confirmPassword", "NewPass123!"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgotPassword"))
                .andExpect(model().attributeExists("forgotPasswordError"));
    }

    @Test
    void testResetSuccess() throws Exception {
        mockMvc.perform(get("/forgotpassword/resetSuccess"))
                .andExpect(status().isOk())
                .andExpect(view().name("resetSuccess"));
    }
}