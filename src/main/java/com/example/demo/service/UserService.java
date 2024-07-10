package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.Optional;

public interface UserService {

    User findByEmail(String email);

    User save(User user);
}
