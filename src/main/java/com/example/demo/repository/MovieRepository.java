package com.example.demo.repository;


import com.example.demo.models.Movie;
import com.example.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}