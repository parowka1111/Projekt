package com.example.demo.service;

import com.example.demo.interfaces.CommentService;
import com.example.demo.models.Comment;
import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Comment censorSpoilers(Comment comment) {
        if (comment.getIsSpoiler() != null && comment.getIsSpoiler()) {
            comment.setContent(hideLetters(comment.getContent()));
            return comment;
        } else {
            return comment;
        }
    }

    @Override
    public Comment createComment(Comment comment) {
        Long userId = comment.getAuthor().getId();
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found: " + userId));

        comment.setAuthor(existingUser);
        Long titleId = comment.getMovie().getId();
        Movie existingMovie = movieRepository.findById(titleId).orElseThrow(() -> new RuntimeException("Movie not found: " + titleId));
        comment.setMovie(existingMovie);

        isRateValid(comment.getRate());
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> getCommentById(@PathVariable Long id) {
        return commentRepository.findById(id)
                .map(comment -> {
                    comment.getAuthor().setUsername(comment.getAuthor().getUsername());
                    comment.getMovie().setTitle(comment.getMovie().getTitle());
                    return comment;
                });
    }

    @Override
    public Comment updateComment(Comment updatedComment, long id) {
        return commentRepository.findById(id).map(comment -> {
            comment.setContent(updatedComment.getContent());
            comment.setIsSpoiler(updatedComment.getIsSpoiler());
            comment.setRate(updatedComment.getRate());
            return commentRepository.save(comment);
        }).orElseThrow(() -> new RuntimeException("Comment not found " + id));
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
