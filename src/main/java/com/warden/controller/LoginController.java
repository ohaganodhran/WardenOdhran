package com.warden.controller;

import com.warden.dao.UserDao;
import com.warden.entity.User;
import com.warden.service.CredentialService;
import com.warden.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/")
public class LoginController {

    private final UserService userService;
    private final UserDao userDao;
    private final CredentialService credentialService;

    @Autowired
    public LoginController(UserService userService, UserDao userDao, CredentialService credentialService) {
        this.userService = userService;
        this.userDao = userDao;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String onLoad(Model model) {
        model.addAttribute("creds", new Identity());
        model.addAttribute("user", new UserDTO());
        return "index";
    }

    @PostMapping("signup")
    public String signup(@Valid @ModelAttribute("user") UserDTO user,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        if (!user.passwordsMatch()) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }

        if (userService.existsByUsername((user.getUsername()))) {
            result.rejectValue("username", "error.username", "Username already taken");
        }

        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.email", "Email already registered");
        }

        if (result.hasErrors()) {
            model.addAttribute("showSignup", true);
            model.addAttribute("creds", new Identity());
            return "index";
        }

        userService.create(user);
        redirectAttributes.addFlashAttribute("signupSuccess", true);
        redirectAttributes.addFlashAttribute("creds", new Identity());
        redirectAttributes.addFlashAttribute("user", new UserDTO());
        return "redirect:/";
    }

    @PostMapping("login")
    public String login(@ModelAttribute("creds") Identity creds, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(creds.getUsername()).orElse(null);
        if (user == null || !userService.checkPassword(creds.getPassword(), user.getPasswordHash())) {
            redirectAttributes.addFlashAttribute("loginError", "Invalid username or password");
            redirectAttributes.addFlashAttribute("creds", new Identity());
            redirectAttributes.addFlashAttribute("user", new UserDTO());
            return "redirect:/";
        } else {
            session.setAttribute("user", user);
            userDao.updateLastLogin(user.getEmail());
            return "redirect:/dashboard";
        }
    }
}