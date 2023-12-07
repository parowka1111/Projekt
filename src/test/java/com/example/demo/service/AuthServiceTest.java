package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthServiceTest {

    private AuthServiceImpl authService;

    @BeforeEach
    public void init() {
        authService = new AuthServiceImpl();
    }
    @Test
    void encryptPassword() throws NoSuchAlgorithmException {
        String password = "P@ssw0rd";
        String expectedHashedPassword = "b03ddf3ca2e714a6548e7495e2a03f5e824eaac9837cd7f159c67b90fb4b7342";
        String hashedPassword = authService.encryptPassword(password);
        assertEquals(expectedHashedPassword, hashedPassword);
    }

}