package com.example.movieapp.service.impl;

import com.example.movieapp.model.MovieGenre;
import com.example.movieapp.model.exceptions.MovieGenreNotFound;
import com.example.movieapp.repository.MovieGenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieGenreServiceImplTest {

    @Mock
    private MovieGenreRepository movieGenreRepository;

    @InjectMocks
    private MovieGenreServiceImpl movieGenreService;


    @Test
    @DisplayName("Should return false when the given id does not exist")
    void deleteMovieGenreWhenIdDoesNotExistReturnFalse() {
        Long movieGenreId = 1L;
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.empty());

        boolean result = movieGenreService.deleteMovieGenre(movieGenreId);

        assertFalse(result);
    }

    @Test
    @DisplayName("Should delete the movie genre when the given id exists")
    void deleteMovieGenreWhenIdExists() {
        Long movieGenreId = 1L;
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(new MovieGenre("Action")));
        boolean result = movieGenreService.deleteMovieGenre(movieGenreId);
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return true when the movie genre is successfully deleted")
    void deleteMovieGenreWhenSuccessfulReturnTrue() {
        Long movieGenreId = 1L;
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(new MovieGenre("Action")));
        boolean result = movieGenreService.deleteMovieGenre(movieGenreId);
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return empty when the movie genre id does not exist")
    void getMovieGenreWhenIdDoesNotExist() {
        Long movieGenreId = 1L;
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.empty());

        Optional<MovieGenre> result = movieGenreService.getMovieGenre(movieGenreId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the movie genre when the movie genre id exists")
    void getMovieGenreWhenIdExists() {
        Long movieGenreId = 1L;
        MovieGenre movieGenre = new MovieGenre("Action");
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(movieGenre));

        Optional<MovieGenre> result = movieGenreService.getMovieGenre(movieGenreId);

        assertTrue(result.isPresent());
        assertEquals(movieGenre, result.get());
    }

    @Test
    @DisplayName("Should throw an exception when the given id does not exist")
    void updateMovieGenreWhenIdDoesNotExistThenThrowException() {
        Long movieGenreId = 1L;
        String name = "Action";

        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.empty());

        assertThrows(MovieGenreNotFound.class, () -> {
            movieGenreService.updateMovieGenre(movieGenreId, name);
        });
    }

    @Test
    @DisplayName("Should update the movie genre when the given id exists")
    void updateMovieGenreWhenIdExists() {
        Long movieGenreId = 1L;
        String newName = "Action";

        MovieGenre existingMovieGenre = new MovieGenre("Adventure");
        existingMovieGenre.setId(movieGenreId);

        MovieGenre updatedMovieGenre = new MovieGenre(newName);
        updatedMovieGenre.setId(movieGenreId);

        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(existingMovieGenre));
        when(movieGenreRepository.save(updatedMovieGenre)).thenReturn(updatedMovieGenre);

        Optional<MovieGenre> result = movieGenreService.updateMovieGenre(movieGenreId, newName);

        assertTrue(result.isPresent());
        assertEquals(newName, result.get().getName());
    }

    @Test
    @DisplayName("Should return an empty optional when the movie genre name is null")
    void addNewMovieGenreWhenNameIsNull() {
        String genreName = null;

        Optional<MovieGenre> result = movieGenreService.addNewMovieGenre(genreName);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should add a new movie genre and return the created genre")
    void addNewMovieGenre() {
        String genreName = "Action";
        MovieGenre newGenre = new MovieGenre(genreName);
        when(movieGenreRepository.save(newGenre)).thenReturn(newGenre);

        Optional<MovieGenre> result = movieGenreService.addNewMovieGenre(genreName);

        assertTrue(result.isPresent());
        assertEquals(genreName, result.get().getName());
    }

    @Test
    @DisplayName("Should return all movie genres")
    void findAllMovieGenres() {
        List<MovieGenre> expectedGenres = List.of(
                new MovieGenre("Action"),
                new MovieGenre("Comedy"),
                new MovieGenre("Drama")
        );

        when(movieGenreRepository.findAll()).thenReturn(expectedGenres);

        List<MovieGenre> actualGenres = movieGenreService.findAllMovieGenres();

        assertEquals(expectedGenres.size(), actualGenres.size());
        assertTrue(actualGenres.containsAll(expectedGenres));
    }
}