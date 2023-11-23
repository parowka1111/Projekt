package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.models.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8000")
@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Comment> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            comment.getAuthor().setUsername(comment.getAuthor().getUsername());
        }
        return comments;
    }

    @GetMapping("/{id}")
    public Optional<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        comment.ifPresent(value -> value.getAuthor().setUsername(value.getAuthor().getUsername()));
        return comment;
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        Long userId = comment.getAuthor().getId();
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        comment.setAuthor(existingUser);
        return commentRepository.save(comment);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        return commentRepository.findById(id)
                .map(comment -> {
                    User updatedUser = updatedComment.getAuthor();
                    User newUser = userRepository.findById(updatedUser.getId())
                            .orElseThrow(() -> new RuntimeException("User not found " + updatedUser.getId()));
                    comment.setAuthor(newUser);
                    comment.setContent(updatedComment.getContent());
                    comment.setIsSpoiler(updatedComment.getIsSpoiler());
                    return commentRepository.save(comment);
                })
                .orElseThrow(() -> new RuntimeException("Comment not found " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }

}
