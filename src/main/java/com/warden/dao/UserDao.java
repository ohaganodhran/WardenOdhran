/**
 * Copyright 2013 Okotech Ltd. 
 * Registered address: 2 Skerry Dhu,Newtowncrommelin,Ballymena,Co.Antrim 
 * All Rights Reserved.
 * 
 * This software, including all software code and architecture, is the confidential and 
 * proprietary information of Okotech Ltd ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with Okotech Ltd.
 */
package com.warden.dao;


import com.warden.entity.User;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * JPA implementation for user entities
 *
 * @author odhran
 *
 */
@Repository
public class UserDao extends AGenericDao<User> implements IUserDao {

    @Transactional
    @Override
    public void updateLastLogin(String email) {
        Query query = em.createQuery("UPDATE User "
                + "SET lastLogin = :date "
                + "WHERE email = :email");
        query.setParameter("date", new Date())
             .setParameter("email", email).executeUpdate();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Query query = em.createQuery("FROM User as u where lower(u.email)= :email");
        query.setParameter("email", (email != null)?email.toLowerCase():null);

        List<User> users = query.getResultList();

        if (users == null || users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = em.createQuery("FROM User as u where u.username= :uName");
        query.setParameter("uName", username);
        List<User> users = query.getResultList();
        if (users == null || users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }
}
