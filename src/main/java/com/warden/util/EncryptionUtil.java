package com.warden.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtil {
    private final SecretKeySpec secretKey;
    private static final String ALGORITHM = "AES";

    public EncryptionUtil(@Value("${encryption.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(), ALGORITHM);
    }

    public String encrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes()));
        } catch(Exception e) {
            throw new RuntimeException("Error encrypting content", e);
        }
    }

    public String decrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(content)));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting content", e);
        }
    }
}
