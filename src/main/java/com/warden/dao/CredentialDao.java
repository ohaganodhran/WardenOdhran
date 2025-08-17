package com.warden.dao;

import com.warden.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CredentialDao extends JpaRepository<Credential, Long> {
    List<Credential> findByUserUsername(String username);
}
