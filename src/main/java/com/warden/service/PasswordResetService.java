package com.warden.service;

import com.warden.dao.PasswordResetTokenRepository;
import com.warden.entity.PasswordResetToken;
import com.warden.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    public void sendResetEmail(String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        String rawToken = generateSecureToken();

        String tokenHash = DigestUtils.sha256Hex(rawToken);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setTokenHash(tokenHash);
        passwordResetToken.setUser(user);
        passwordResetToken.setCreatedAt(LocalDateTime.now());
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        passwordResetToken.setUsed(false);

        passwordResetTokenRepository.save(passwordResetToken);

        String link = "http://localhost:8080/forgotpassword/reset?token=" + rawToken;
        emailService.sendEmail(user.getEmail(), "Reset your password", "Click the link to reset your password: " + link);
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Optional<PasswordResetToken> verifyRawToken(String rawToken) {
        String tokenHash = DigestUtils.sha256Hex(rawToken);
        return  passwordResetTokenRepository.findByTokenHash(tokenHash)
                .filter(token -> !token.isUsed() && token.getExpiresAt().isAfter(LocalDateTime.now()));
    }

    public void markTokenUsed(PasswordResetToken token) {
        token.setUsed(true);
        passwordResetTokenRepository.save(token);
    }

    public void resetPassword(PasswordResetToken tokenEntity, String newPassword) {
        User user = tokenEntity.getUser();

        userService.updatePassword(user, newPassword);

        markTokenUsed(tokenEntity);
    }
}
