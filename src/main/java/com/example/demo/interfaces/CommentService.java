package com.example.demo.interfaces;

import com.example.demo.models.Comment;

public interface CommentService {
    Comment censorSpoilers(Comment comment);

    String hideLetters(String text);

    boolean isRateValid(int rate);

    Comment createComment(Comment comment);

    Comment updateComment(Comment comment, long id);
}
