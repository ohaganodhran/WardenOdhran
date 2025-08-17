package com.warden.service;

import com.warden.dao.CredentialDao;
import com.warden.entity.Credential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CredentialService {

    private final CredentialDao credentialDao;

    @Autowired
    public CredentialService(CredentialDao credentialDao) {
        this.credentialDao = credentialDao;
    }

    public List<Credential> findByUsername(String username) {
        return credentialDao.findByUserUsername(username);
    }

    public Credential saveCredential(Credential credential) {
        return credentialDao.save(credential);
    }

    public void deleteCredential(Long id) {
        credentialDao.deleteById(id);
    }

}
