package com.example.movieapp.service;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieGenre;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Optional<Movie> addNewMovie(String name, String description, double price, Long movieGenreId, String imageUrl, List<Long> actorsId);

    Movie getMovie(Long id);

    Optional<Movie>updateMovie(Long id, String name, String description, double price, Long movieGenreId, String imageUrl, List<Long> actorsId);

    List<Movie> getAllMovies();

    List<Movie> findAllMoviesFromMovieGenre(Long movieGenreId);

    boolean deleteMovie(Long movieId);

    int getNumberOfMoviesFromGenre(Long movieGenreId);


}
