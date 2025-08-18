package com.warden.controller;

import com.warden.entity.Credential;
import com.warden.entity.User;
import com.warden.service.CredentialService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class CredentialController {

    private final CredentialService credentialService;

    @Autowired
    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/";
        }

        List<Credential> credentials = credentialService.findByUsername(currentUser.getUsername());
        model.addAttribute("credentials", credentials);
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("newCredential", new Credential());
        return "dashboard";
    }

    @PostMapping("/credential/add")
    public String addCredential(@ModelAttribute("newCredential") CredentialDTO credential, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        credential.setUser(user);
        credentialService.saveCredential(credential);

        return "redirect:/dashboard";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
