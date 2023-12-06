package com.example.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @ManyToOne
    private User author;
    private Boolean isSpoiler;
    @ManyToOne
    private Movie movie;

    private Integer rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Boolean getIsSpoiler() {
        return isSpoiler;
    }

    public void setIsSpoiler(Boolean isSpoiler) {
        this.isSpoiler = isSpoiler;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

}
