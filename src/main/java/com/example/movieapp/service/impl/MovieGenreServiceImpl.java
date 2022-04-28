package com.example.movieapp.service.impl;

import com.example.movieapp.model.MovieGenre;
import com.example.movieapp.model.exceptions.MovieGenreNotFound;
import com.example.movieapp.repository.MovieGenreRepository;
import com.example.movieapp.service.MovieGenreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieGenreServiceImpl implements MovieGenreService {

    private final MovieGenreRepository movieGenreRepository;

    public MovieGenreServiceImpl(MovieGenreRepository movieGenreRepository) {
        this.movieGenreRepository = movieGenreRepository;
    }

    @Override
    public List<MovieGenre> findAllMovieGenres() {
        return this.movieGenreRepository.findAll();
    }

    @Override
    public Optional<MovieGenre> addNewMovieGenre(String name) {
        MovieGenre movieGenre= new MovieGenre(name);
        return Optional.of(this.movieGenreRepository.save(movieGenre));
    }

    @Override
    public Optional<MovieGenre> updateMovieGenre(Long movieGenreId, String name) {
        MovieGenre movieGenre=this.movieGenreRepository.findById(movieGenreId).orElseThrow(MovieGenreNotFound::new);
        movieGenre.setName(name);
        return Optional.of(this.movieGenreRepository.save(movieGenre));
    }

    @Override
    public Optional<MovieGenre> getMovieGenre(Long movieGenreId) {
        return this.movieGenreRepository.findById(movieGenreId);
    }

    @Override
    public boolean deleteMovieGenre(Long movieGenreId) {
        this.movieGenreRepository.deleteById(movieGenreId);
        return this.movieGenreRepository.findById(movieGenreId).isEmpty();
    }
}
