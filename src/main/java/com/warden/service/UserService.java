package com.warden.service;


import com.warden.controller.UserDTO;
import com.warden.dao.IUserDao;
import com.warden.entity.User;
import lombok.extern.slf4j.Slf4j;
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

    @Autowired
    public UserService(IUserDao userDao) {
        this.userDao = userDao;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void create(UserDTO userDTO) {
        User user = mapToEntity(userDTO);
        userDao.create(user);
        log.info("User created: {}", user.getUsername());
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
    }
}
