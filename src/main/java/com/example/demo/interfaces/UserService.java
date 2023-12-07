package com.example.demo.interfaces;

import com.example.demo.models.User;

public interface UserService {
    String getHashedPassword(String password, String salt);

    User registerUser(User user);

    Long loginUser(String usernameOrEmail, String password);

    boolean isPasswordValid(String password);

    boolean isEmailValid(String email);
}
