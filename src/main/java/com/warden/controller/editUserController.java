package com.warden.controller;

import com.warden.entity.User;
import com.warden.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class editUserController {

    private final UserService userService;

    public editUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/editUser")
    public String onLoad(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/";
        }

        User user = userService.findByUsername(currentUser.getUsername()).orElse(null);
        model.addAttribute("userToEdit", user);

        return "editUser";
    }

    @PostMapping("/editUser/edit")
    public String editUser(@ModelAttribute("userToEdit") UserDTO userDTO,
                           HttpSession session,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        long id = user.getId();

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (userDTO.getEmail() == null || !userDTO.getEmail().matches(emailRegex)) {
            result.rejectValue("email", "error.email", "Email must be in valid format");
        }

        if(!userService.doesEmailExist(id, userDTO.getEmail())) {
            result.rejectValue("email", "error.email", "Email already registered");
        }

        if(userDTO.getUsername() == null || userDTO.getUsername().isBlank()) {
            result.rejectValue("username", "error.username", "Username cannot be blank");
        }
        if(!userService.doesUsernameExist(id, userDTO.getUsername())) {
            result.rejectValue("username", "error.username", "Username already taken");
        }

        if (result.hasFieldErrors()) {
            userDTO.setModified(user.getModified());
            return "editUser";
        }

        userService.update(id, userDTO);

        User updatedUser = (User) session.getAttribute("user");
        updatedUser.setUsername(userDTO.getUsername());
        updatedUser.setEmail(userDTO.getEmail());
        updatedUser.setModified(userDTO.getModified());

        session.setAttribute("user", updatedUser);
        redirectAttributes.addFlashAttribute("editSuccess", true);

        return "redirect:/editUser?editSuccess=true";
    }
}
