package com.example.demo.interfaces;

public interface AuthService {
    String encryptPassword(String password);

    String generateSalt();
}