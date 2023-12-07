package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    public void init() {
        userService = new UserServiceImpl();
    }

    @Test
    public void testIsPasswordValid() {
        assertTrue(userService.isPasswordValid("P@ssw0rd"));
        assertTrue(userService.isPasswordValid("Abc!Defghi2"));
        assertFalse(userService.isPasswordValid("Password"));
        assertFalse(userService.isPasswordValid("pass"));
    }

    @Test
    public void testIsEmailValid() {
        assertTrue(userService.isEmailValid("email@email.com"));
        assertTrue(userService.isEmailValid("Email2@Email2.pl"));
        assertFalse(userService.isEmailValid("email@"));
        assertFalse(userService.isEmailValid("email"));
    }

    @Test
    public void testAssignRole() {
        assertEquals(userService.assignRole("Email@ServiceAdmin.pl"), "ADMIN");
        assertEquals(userService.assignRole("Email@Guest.pl"), "GUEST");
        assertNotEquals(userService.assignRole("Email@serviceAdmiin.pl"), "ADMIN");
        assertNotEquals(userService.assignRole("@ServiceAdmin.pl"), "GUEST");
    }

}