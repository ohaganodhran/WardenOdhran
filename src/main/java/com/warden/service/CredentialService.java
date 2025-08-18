package com.warden.service;

import com.warden.controller.CredentialDTO;
import com.warden.controller.UserDTO;
import com.warden.dao.CredentialDao;
import com.warden.entity.Credential;
import com.warden.entity.User;
import com.warden.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CredentialService {

    private final CredentialDao credentialDao;
    private final EncryptionUtil encryptionUtil;


    @Autowired
    public CredentialService(CredentialDao credentialDao, EncryptionUtil encryptionUtil) {
        this.credentialDao = credentialDao;
        this.encryptionUtil = encryptionUtil;
    }

    public List<Credential> findByUsername(String username) {
        List<Credential> credentials = credentialDao.findByUserUsername(username);
        credentials.forEach(credential -> credential.setPasswordHash(encryptionUtil.decrypt(credential.getPasswordHash())));
        return credentials;
    }

    public void saveCredential(CredentialDTO credentialDTO) {
        Credential credential1 = mapToEntity(credentialDTO);
        credentialDao.save(credential1);
        log.info("Credential created under user {}", credential1.getUsername());
    }

    public void deleteCredential(Long id) {
        credentialDao.deleteById(id);
    }

    public Credential mapToEntity(CredentialDTO credentialDTO) {
        Credential credential = new Credential();
        credential.setId(credentialDTO.getId());
        credential.setUser(credentialDTO.getUser());
        credential.setSiteName(credentialDTO.getSiteName());
        credential.setUsername(credentialDTO.getUsername());
        credential.setNotes(credentialDTO.getNotes());

        credential.setPasswordHash(encryptionUtil.encrypt(credentialDTO.getPasswordHash()));

        return credential;
    }
}
