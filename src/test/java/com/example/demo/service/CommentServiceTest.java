package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    private CommentServiceImpl commentService;

    @BeforeEach
    public void init() {
        commentService = new CommentServiceImpl();
    }

    @Test
    public void testIsRateValid() {
        assertTrue(commentService.isRateValid(1));
        assertTrue(commentService.isRateValid(5));
        assertThrows(RuntimeException.class, () -> commentService.isRateValid(6));
        assertThrows(RuntimeException.class, () -> commentService.isRateValid(0));
    }

    @Test
    public void testHideLetters() {
        assertEquals(commentService.hideLetters("okej lets goł"), "**** **** ***");
        assertEquals(commentService.hideLetters("Lorem Ipsum is simply dummy text of the printing and typesetting industry"), "***** ***** ** ****** ***** **** ** *** ******** *** *********** ********");
        assertNotEquals(commentService.hideLetters("To nie powinno działać"), "To nie powinno działać");
        assertNotEquals(commentService.hideLetters("To nie powinno działać"), "**********************");
    }
}
