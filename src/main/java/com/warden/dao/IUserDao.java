package com.warden.dao;

import com.warden.entity.User;

import java.util.Optional;

/**
 * CRUD for user operations
 *
 * @author odhran
 *
 */
public interface IUserDao extends IGenericDao<User> {
    void updateLastLogin(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}