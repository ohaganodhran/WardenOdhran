package com.warden.service;


import com.warden.controller.UserDTO;
import com.warden.dao.IUserDao;
import com.warden.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final IUserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public UserService(IUserDao userDao) {
        this.userDao = userDao;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void create(UserDTO userDTO) {
        User user = mapToEntity(userDTO);
        userDao.create(user);
        logger.info("New user created under name '{}'", user.getUsername());
    }

    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    public boolean existsByUsername(String username) {
        return userDao.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userDao.findByEmail(email).isPresent();
    }

    private User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        String hashed = passwordEncoder.encode(userDTO.getPassword());
        user.setPasswordHash(hashed);
        return user;
    }

    public void updatePassword(User user, String newPassword) {
        String hashed = passwordEncoder.encode(newPassword);
        user.setPasswordHash(hashed);
        userDao.update(user);
        logger.info("Password Updated for User '{}'", user.getUsername());
    }

    //fail only if its in the db and belongs to someone else
    public boolean doesUsernameExist(Long id, String username) {
        User user = userDao.findByUsername(username).orElse(new User());
        if(user.getUsername() != null) {
            return user.getId().equals(id);
        }
        return true;
    }

    //fail only if its in the db and belongs to someone else
    public boolean doesEmailExist(Long id, String email) {
        User user = findByEmail(email).orElse(new User());
        if(user.getEmail() != null) {
            return user.getId().equals(id);
        }
        return true;
    }

    public void update(Long id, UserDTO userDTO) {
        User existingUser = Optional.ofNullable(userDao.findById(id))
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());

        userDao.update(existingUser);
        logger.info("User '{}' updated", existingUser.getUsername());
    }

    public void delete(Long id) {
        User user = userDao.findById(id);
        logger.info("User '{}' deleted", user.getUsername());

        try {
            userDao.delete(id);
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage());
        }
    }
}
