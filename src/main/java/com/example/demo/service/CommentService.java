package com.example.demo.service;

import com.example.demo.models.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    public Comment censorSpoilers(Comment comment) {
        if (comment.getIsSpoiler() != null && comment.getIsSpoiler()) {
            comment.setContent(hideLetters(comment.getContent()));
            return comment;
        } else {
            return comment;
        }
    }

    public String hideLetters(String text) {
        return text.replaceAll("[^ ]", "*");
    }

    public boolean isRateValid(int rate) {
        if (rate >= 1 && rate <= 5) {
            return true;
        }
        throw new RuntimeException("invalid rate");
    }
}
