package com.warden.controller;

import com.warden.entity.PasswordResetToken;
import com.warden.entity.User;
import com.warden.service.PasswordResetService;
import com.warden.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @Autowired
    public ForgotPasswordController(UserService userService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @GetMapping
    public String onLoad(Model model){
        model.addAttribute("forgot", new Identity());
        return "forgotPassword";
    }

    @GetMapping("/sent")
    public String sent(){
        return "forgotPasswordSent";
    }

    @PostMapping
    public String forgotPassword(@ModelAttribute("forgot") Identity credentials, RedirectAttributes redirectAttributes){
        String email = credentials.getEmail();

        if(email == null || email.isEmpty()){
            redirectAttributes.addFlashAttribute("forgot", credentials);
            redirectAttributes.addFlashAttribute("forgotPasswordError", "Please enter valid email.");
            return "redirect:/forgotpassword";
        }

        userService.findByEmail(email).ifPresent(user -> passwordResetService.sendResetEmail(user.getEmail()));

        return "redirect:/forgotpassword/sent";
    }

    @GetMapping("/reset")
    public String showResetForm(@RequestParam("token")  String rawToken, Model model, RedirectAttributes redirectAttributes) {
        var tokenOpt = passwordResetService.verifyRawToken(rawToken);

        if(tokenOpt.isEmpty()){
            redirectAttributes.addFlashAttribute("forgotPasswordError", "Invalid token");
            redirectAttributes.addFlashAttribute("forgot", new Identity());
            return "redirect:/forgotPassword";
        }

        model.addAttribute("token", rawToken);
        model.addAttribute("forgotDTO", new ForgotDTO());
        return "resetPassword";
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestParam("token") String rawToken, @Valid @ModelAttribute("forgotDTO") ForgotDTO forgotDTO, BindingResult result, Model model) {

        if(!forgotDTO.passwordsMatch()) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }

        if(result.hasErrors()) {
            model.addAttribute("showSignup", true);
            model.addAttribute("forgot", forgotDTO);
            model.addAttribute("token", rawToken);
            return "resetPassword";
        }

        Optional<PasswordResetToken> tokenOpt = passwordResetService.verifyRawToken(rawToken);
        if(tokenOpt.isEmpty()) {
            model.addAttribute("forgotPasswordError", "Invalid token");
            model.addAttribute("forgot", new Identity());
            return "forgotPassword";
        }

        PasswordResetToken tokenEntity = tokenOpt.get();
        passwordResetService.resetPassword(tokenEntity, forgotDTO.getNewPassword());
        passwordResetService.markTokenUsed(tokenEntity);

        return "redirect:/forgotpassword/resetSuccess";
    }

    @GetMapping("/resetSuccess")
    public String restSuccess() {
        return "resetSuccess";
    }
}
