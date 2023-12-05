package com.example.demo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    @Test
    public void testIsRateValid() {

        assertTrue(CommentService.isRateValid(1));
        assertTrue(CommentService.isRateValid(5));
        assertFalse(CommentService.isRateValid(6));
        assertFalse(CommentService.isRateValid(0));
    }

}