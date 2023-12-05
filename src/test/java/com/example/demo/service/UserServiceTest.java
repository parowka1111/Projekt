package com.example.demo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    public void testIsPasswordValid() {
        UserService userService = new UserService();

        assertTrue(userService.isPasswordValid("P@ssw0rd"));
        assertTrue(userService.isPasswordValid("Abc!Defg2"));
        assertFalse(userService.isPasswordValid("password"));
        assertFalse(userService.isPasswordValid("Passw0rd"));
    }

    @Test
    public void testIsEmailValid() {
        UserService userService = new UserService();

        assertTrue(userService.isEmailValid("email@email.com"));
        assertTrue(userService.isEmailValid("Email2@Email2.pl"));
        assertFalse(userService.isEmailValid("email@"));
        assertFalse(userService.isEmailValid("email"));
    }

}