package com.warden.controller;

import com.warden.dao.UserDao;
import com.warden.entity.User;
import com.warden.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class TestLoginController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDao userDao;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUsername("odhran");
        mockUser.setEmail("odhran@example.com");
        mockUser.setPasswordHash("hashedPassword");
    }

    @Test
    void testLogin_Success() throws Exception {
        when(userService.findByUsername("odhran"))
                .thenReturn(Optional.of(mockUser));
        when(userService.checkPassword("secret", "hashedPassword"))
                .thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("username", "odhran")
                        .param("password", "secret"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void testLogin_Failure() throws Exception {
        when(userService.findByUsername("wronguser"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .param("username", "wronguser")
                        .param("password", "badpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("loginError"));
    }

    @Test
    void testSignup_Success() throws Exception {
        // no conflicts
        when(userService.existsByUsername("odhran")).thenReturn(false);
        when(userService.existsByEmail("odhran@example.com")).thenReturn(false);
        doNothing().when(userService).create(Mockito.any());

        mockMvc.perform(post("/signup")
                        .param("username", "odhran")
                        .param("email", "odhran@example.com")
                        .param("password", "MyPass!word")
                        .param("confirmPassword", "MyPass!word"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("signupSuccess", true));
    }

    @Test
    void testSignup_Fail_UsernameTaken() throws Exception {
        when(userService.existsByUsername("odhran")).thenReturn(true);

        mockMvc.perform(post("/signup")
                        .param("username", "odhran")
                        .param("email", "odhran@example.com")
                        .param("password", "MyPass!word")
                        .param("confirmPassword", "MyPass!word"))
                .andExpect(status().isOk()) // stays on index
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("creds"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("showSignup", true));
    }

    @Test
    void testSignup_Fail_PasswordsDontMatch() throws Exception {
        when(userService.existsByUsername("odhran")).thenReturn(false);
        when(userService.existsByEmail("odhran@example.com")).thenReturn(false);

        mockMvc.perform(post("/signup")
                        .param("username", "odhran")
                        .param("email", "odhran@example.com")
                        .param("password", "MyPass!word")
                        .param("confirmPassword", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("showSignup"));
    }

    @Test
    void testOnLoad_NoUserInSession() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("creds"))
                .andExpect(model().attributeExists("user"));
    }
}
