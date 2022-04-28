package com.example.movieapp.service.impl;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieGenre;
import com.example.movieapp.model.exceptions.MovieGenreNotFound;
import com.example.movieapp.model.exceptions.MovieNotFound;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.repository.MovieGenreRepository;
import com.example.movieapp.repository.MovieRepository;
import com.example.movieapp.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieGenreRepository movieGenreRepository;

    public MovieServiceImpl(MovieRepository movieRepository, ActorRepository actorRepository, MovieGenreRepository movieGenreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.movieGenreRepository = movieGenreRepository;
    }

    @Override
    public Optional<Movie> addNewMovie(String name, String description, double price, Long movieGenreId, String imageUrl, List<Long> actorsId) {
        List<Actor> actors= this.actorRepository.findAllById(actorsId);
        MovieGenre movieGenre =this.movieGenreRepository.findById(movieGenreId).orElseThrow(MovieGenreNotFound::new);
        Movie movie= new Movie(name,description,price,movieGenre,imageUrl,actors);
        return Optional.of(this.movieRepository.save(movie));
    }

    @Override
    public Movie getMovie(Long id) {
        return this.movieRepository.findById(id).orElseThrow(MovieNotFound::new);
    }

    @Override
    public Optional<Movie> updateMovie(Long id, String name, String description, double price, Long movieGenreId, String imageUrl, List<Long> actorsId) {
        List<Actor> actors= this.actorRepository.findAllById(actorsId);
        MovieGenre movieGenre =this.movieGenreRepository.findById(movieGenreId).orElseThrow(MovieGenreNotFound::new);
        Movie movie=this.getMovie(id);
        movie.setName(name);
        movie.setDescription(description);
        movie.setPrice(price);
        movie.setMovieGenre(movieGenre);
        movie.setImageUrl(imageUrl);
        movie.setActors(actors);
        return Optional.of(this.movieRepository.save(movie));
    }

    @Override
    public List<Movie> getAllMovies() {
        return this.movieRepository.findAll();
    }

    @Override
    public List<Movie> findAllMoviesFromMovieGenre(Long movieGenreId) {
        MovieGenre movieGenre =this.movieGenreRepository.findById(movieGenreId).orElseThrow(MovieGenreNotFound::new);
        return this.movieRepository.findAllByMovieGenre(movieGenre);
    }

    @Override
    public boolean deleteMovie(Long movieId) {
        Movie movie = this.getMovie(movieId);
        this.movieRepository.delete(movie);
        return this.movieRepository.findById(movieId).isEmpty();
    }

    @Override
    public int getNumberOfMoviesFromGenre(Long movieGenreId) {
        return this.findAllMoviesFromMovieGenre(movieGenreId).size();
    }
}
