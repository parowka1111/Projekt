package com.example.demo.service;

import com.example.demo.models.Comment;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommentService {
    public Comment censorSpoilers(Comment comment) {
        if (comment.getIsSpoiler() != null && comment.getIsSpoiler()) {
            String censoredContent = comment.getContent().replaceAll("[^ ]", "*");
            Comment censoredComment = new Comment();
            censoredComment.setId(comment.getId());
            censoredComment.setContent(censoredContent);
            censoredComment.setAuthor(comment.getAuthor());
            censoredComment.setIsSpoiler(comment.getIsSpoiler());
            censoredComment.setMovie(comment.getMovie());
            censoredComment.setRate(comment.getRate());
            return censoredComment;
        } else {
            return comment;
        }
    }

    static boolean isRateValid(int rate) {
        if(rate >= 1 && rate <= 5){
            return true;
        }
        return false;
    }
}
