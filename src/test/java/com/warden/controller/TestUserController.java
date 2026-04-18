package com.warden.controller;

import com.warden.entity.User;
import com.warden.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class TestUserController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User mockUser;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("odhran");
        mockUser.setEmail("odhran@example.com");
        mockUser.setPasswordHash("hashedPassword");

        session = new MockHttpSession();
        session.setAttribute("user", mockUser);
    }

    /// //////////////////////////////////////////////////////////
    /// ///////////////////////EDIT TESTS//////////////////////////
    /// //////////////////////////////////////////////////////////

    @Test
    void testEditUser_Get_WithSession() throws Exception {
        when(userService.findByUsername("odhran")).thenReturn(java.util.Optional.of(mockUser));

        mockMvc.perform(get("/editUser").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("editUser"))
                .andExpect(model().attributeExists("userToEdit"));
    }

    @Test
    void testEditUser_Get_NoSession() throws Exception {
        mockMvc.perform(get("/editUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testEditUser_Put_Success() throws Exception {
        when(userService.doesEmailExist(1L, "newemail@example.com")).thenReturn(true);
        when(userService.doesUsernameExist(1L, "newusername")).thenReturn(true);
        Mockito.doNothing().when(userService).update(Mockito.eq(1L), Mockito.any());

        mockMvc.perform(put("/editUser").session(session)
                        .param("username", "newusername")
                        .param("email", "newemail@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/editUser?editSuccess=true"));
    }

    @Test
    void testEditUser_Put_NoSession() throws Exception {
        mockMvc.perform(put("/editUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testEditUser_Put_InvalidEmail() throws Exception {
        when(userService.doesUsernameExist(1L, "odhran")).thenReturn(true);

        mockMvc.perform(put("/editUser").session(session)
                        .param("username", "odhran")
                        .param("email", "notanemail"))
                .andExpect(status().isOk())
                .andExpect(view().name("editUser"));
    }

    @Test
    void testEditUser_Put_UsernameTaken() throws Exception {
        when(userService.doesEmailExist(1L, "odhran@example.com")).thenReturn(true);
        when(userService.doesUsernameExist(1L, "odhran")).thenReturn(false);

        mockMvc.perform(put("/editUser").session(session)
                        .param("username", "odhran")
                        .param("email", "odhran@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("editUser"));
    }

    @Test
    void testEditUser_Put_EmailTaken() throws Exception {
        when(userService.doesEmailExist(1L, "taken@example.com")).thenReturn(false);
        when(userService.doesUsernameExist(1L, "odhran")).thenReturn(true);

        mockMvc.perform(put("/editUser").session(session)
                        .param("username", "odhran")
                        .param("email", "taken@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("editUser"));
    }

    /// //////////////////////////////////////////////////////////
    /// /////////////////////DELETE TESTS//////////////////////////
    /// //////////////////////////////////////////////////////////

    @Test
    void testDeleteUser_Get_WithSession() throws Exception {
        mockMvc.perform(get("/deleteUser").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("deleteUser"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testDeleteUser_Get_NoSession() throws Exception {
        mockMvc.perform(get("/deleteUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testDeleteUser_Post_CorrectPassword_PasswordsMatch() throws Exception {
        when(userService.checkPassword("MyPass!1", "hashedPassword")).thenReturn(true);

        mockMvc.perform(post("/deleteUser").session(session)
                        .param("password", "MyPass!1")
                        .param("confirmPassword", "MyPass!1"))
                .andExpect(status().isOk())
                .andExpect(view().name("deleteUser"))
                .andExpect(model().attribute("showConfirmModal", true));
    }

    @Test
    void testDeleteUser_Post_WrongPassword() throws Exception {
        when(userService.checkPassword("wrongpass", "hashedPassword")).thenReturn(false);

        mockMvc.perform(post("/deleteUser").session(session)
                        .param("password", "wrongpass")
                        .param("confirmPassword", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("deleteUser"))
                .andExpect(model().attributeDoesNotExist("showConfirmModal"));
    }

    @Test
    void testDeleteUser_Post_PasswordsDontMatch() throws Exception {
        when(userService.checkPassword("MyPass!1", "hashedPassword")).thenReturn(true);

        mockMvc.perform(post("/deleteUser").session(session)
                        .param("password", "MyPass!1")
                        .param("confirmPassword", "different"))
                .andExpect(status().isOk())
                .andExpect(view().name("deleteUser"))
                .andExpect(model().attributeDoesNotExist("showConfirmModal"));
    }

    @Test
    void testDeleteUser_Confirm_Success() throws Exception {
        Mockito.doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/deleteUser/confirm").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(userService).delete(1L);
    }

    @Test
    void testDeleteUser_Confirm_InvalidatesSession() throws Exception {
        Mockito.doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/deleteUser/confirm").session(session))
                .andExpect(status().is3xxRedirection());

        assert session.isInvalid();
    }
}