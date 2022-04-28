package com.example.movieapp.service;

import com.example.movieapp.model.MovieGenre;

import java.util.List;
import java.util.Optional;

public interface MovieGenreService {

    List<MovieGenre> findAllMovieGenres();

    Optional<MovieGenre> addNewMovieGenre(String name);

    Optional<MovieGenre> updateMovieGenre(Long movieGenreId, String name );

    Optional<MovieGenre> getMovieGenre(Long movieGenreId);

    boolean deleteMovieGenre(Long movieGenreId);

}
