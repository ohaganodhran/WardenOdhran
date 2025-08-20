package com.warden.service;

import com.warden.controller.CredentialDTO;
import com.warden.dao.CredentialDao;
import com.warden.entity.Credential;
import com.warden.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Credential findById(Long id) {
        return credentialDao.findById(id).orElse(null);
    }

    public void saveCredential(CredentialDTO credentialDTO) {
        Credential credential = mapToEntity(credentialDTO);
        credentialDao.save(credential);
        log.info("Credential created under user {}", credential.getUsername());
    }

    public void deleteCredential(Long id) {
        credentialDao.findById(id).ifPresent(credential -> log.info("Credential deleted under user {}", credential.getUsername()));
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
