package com.example.movieapp.repository;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {

    List<Movie> findAllByMovieGenre(MovieGenre movieGenre);

}
