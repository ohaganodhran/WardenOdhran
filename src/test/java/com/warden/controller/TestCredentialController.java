package com.warden.controller;

import com.warden.entity.Credential;
import com.warden.entity.User;
import com.warden.service.CredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CredentialController.class)
class TestCredentialController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CredentialService credentialService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUsername("odhran");
        mockUser.setEmail("odhran@example.com");

        List<Credential> mockCredentials;

        Credential cred1 = new Credential();
        cred1.setId(1L);
        cred1.setSiteName("example.com");
        cred1.setUsername("user1");

        Credential cred2 = new Credential();
        cred2.setId(2L);
        cred2.setSiteName("test.com");
        cred2.setUsername("user2");

        mockCredentials = Arrays.asList(cred1, cred2);
    }

    @Test
    void testDashboard_UserInSession() throws Exception {
        mockMvc.perform(get("/dashboard")
                        .sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("credentials"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("newCredential"))
                .andExpect(model().attributeExists("credentialToEdit"));
    }

    @Test
    void testDashboard_NoUserInSession() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testAddCredential_UserInSession() throws Exception {
        doNothing().when(credentialService).saveCredential(any());

        mockMvc.perform(post("/credential/add")
                        .sessionAttr("user", mockUser)
                        .param("site", "example.com")
                        .param("username", "user1")
                        .param("password", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void testAddCredential_NoUserInSession() throws Exception {
        mockMvc.perform(post("/credential/add")
                        .param("site", "example.com")
                        .param("username", "user1")
                        .param("password", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testEditCredential_UserInSession() throws Exception {
        doNothing().when(credentialService).updateCredential(anyLong(), any());

        mockMvc.perform(post("/credential/edit")
                        .sessionAttr("user", mockUser)
                        .param("id", "1")
                        .param("site", "example.com")
                        .param("username", "user1")
                        .param("password", "newpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard?editSuccess=true"));
    }

    @Test
    void testEditCredential_NoUserInSession() throws Exception {
        mockMvc.perform(post("/credential/edit")
                        .param("id", "1")
                        .param("site", "example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testDeleteCredential_UserInSession() throws Exception {
        doNothing().when(credentialService).deleteCredential(anyLong());

        mockMvc.perform(post("/credential/delete")
                        .sessionAttr("user", mockUser)
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard?deletionSuccess=true"));
    }

    @Test
    void testDeleteCredential_NoUserInSession() throws Exception {
        mockMvc.perform(post("/credential/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testLogout_WithSession() throws Exception {
        mockMvc.perform(post("/logout")
                        .sessionAttr("user", mockUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}