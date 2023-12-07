package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8000")
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired

    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CommentServiceImpl commentService;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @GetMapping("/hiddenspoilers")
    public List<Comment> getAllComments_hideSpoilers() {
        List<Comment> comments = commentRepository.findAll();
        List<Comment> convertedComments = comments.stream().map(commentService::censorSpoilers).collect(Collectors.toList());
        return convertedComments;
    }

    @GetMapping("/movie/{id}/{isSpoiler}")
    public List<Comment> getAllComments_byMovie(@PathVariable Long id, @PathVariable boolean isSpoiler) {
        List<Comment> comments = commentRepository.findAll();
        List<Comment> filteredComments = comments.stream().filter(comment -> comment.getMovie() != null && comment.getMovie().getId().equals(id)).map(comment -> {
            if (isSpoiler) {
                return commentService.censorSpoilers(comment);
            } else {
                return comment;
            }
        }).collect(Collectors.toList());
        return filteredComments;
    }

    @GetMapping("/{id}")
    public Optional<Comment> getCommentById(@PathVariable Long id) {
        return commentRepository.findById(id)
                .map(comment -> {
                    comment.getAuthor().setUsername(comment.getAuthor().getUsername());
                    comment.getMovie().setTitle(comment.getMovie().getTitle());
                    return comment;
                });
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        return commentService.updateComment(updatedComment, id);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }

}
