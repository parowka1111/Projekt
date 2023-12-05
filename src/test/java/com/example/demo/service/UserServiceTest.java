package com.example.demo.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    public void testIsEmailValid() {
        UserService userService = new UserService();

        assertTrue(userService.isEmailValid("email@email.com"));
        assertTrue(userService.isEmailValid("Email2@Email2.pl"));
        assertFalse(userService.isEmailValid("email@"));
        assertFalse(userService.isEmailValid("email"));
    }

}