package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.models.Movie;
import com.example.demo.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@BootstrapWith(SpringBootTestContextBootstrapper.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllComments() throws Exception {

        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/comments"))
                .andExpect(status().isOk())
                .andReturn();


        String content = getResult.getResponse().getContentAsString();
        List<Comment> comments = objectMapper.readValue(content, List.class);
        assertFalse(comments.isEmpty());
    }


    @Test
    public void testCreateComment() throws Exception {
        Comment newComment = new Comment();
        newComment.setContent("Test Comment Content");
        newComment.setIsSpoiler(false);
        newComment.setRate(5);

        User author = new User();
        author.setId(1L);
        newComment.setAuthor(author);

        Movie movie = new Movie();
        movie.setId(1L);
        newComment.setMovie(movie);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.content").value("Test Comment Content"))
                .andExpect(jsonPath("$.isSpoiler").value(false))
                .andExpect(jsonPath("$.rate").value(5));
    }

}