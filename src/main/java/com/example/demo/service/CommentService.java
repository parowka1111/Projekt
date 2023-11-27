package com.example.demo.service;

import com.example.demo.models.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    public Comment convertCommentContent(Comment comment) {
        if (comment.getIsSpoiler() != null && comment.getIsSpoiler()) {
            String censoredContent = comment.getContent().replaceAll("[^ ]", "*");
            Comment censoredComment = new Comment();
            censoredComment.setId(comment.getId());
            censoredComment.setContent(censoredContent);
            censoredComment.setAuthor(comment.getAuthor());
            censoredComment.setIsSpoiler(comment.getIsSpoiler());
            return censoredComment;
        } else {
            return comment;
        }
    }
}
