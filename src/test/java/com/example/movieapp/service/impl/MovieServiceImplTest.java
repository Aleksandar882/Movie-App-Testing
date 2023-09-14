package com.example.movieapp.service.impl;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieGenre;
import com.example.movieapp.model.exceptions.MovieGenreNotFound;
import com.example.movieapp.model.exceptions.MovieNotFound;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.repository.MovieGenreRepository;
import com.example.movieapp.repository.MovieRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private MovieGenreRepository movieGenreRepository;

    @InjectMocks
    private MovieServiceImpl movieService;


    @Test
    @DisplayName("Should return zero when there are no movies for a given genre")
    void getNumberOfMoviesFromGenreReturnsZeroWhenNoMovies() {
        Long movieGenreId = 1L;
        MovieGenre movieGenre = new MovieGenre("Action");
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(movieGenre));
        when(movieRepository.findAllByMovieGenre(movieGenre)).thenReturn(new ArrayList<>());

        int numberOfMovies = movieService.getNumberOfMoviesFromGenre(movieGenreId);

        assertEquals(0, numberOfMovies);
    }


    @Test
    @DisplayName("Should throw an exception when the genre does not exist")
    void getNumberOfMoviesFromGenreThrowsExceptionWhenGenreNotFound() {
        Long movieGenreId = 1L;
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(MovieGenreNotFound.class, () -> {
            movieService.getNumberOfMoviesFromGenre(movieGenreId);
        });

        // Verify
        verify(movieGenreRepository, times(1)).findById(movieGenreId);
        verifyNoMoreInteractions(movieRepository, actorRepository, movieGenreRepository);
    }

    @Test
    @DisplayName("Should return true when the movie is successfully deleted")
    void deleteMovieReturnTrueWhenSuccessful() {
        Long movieId = 1L;
        List<Actor> actors = Arrays.asList(
                new Actor("Actor 1"),
                new Actor("Actor 2")
        );
        Movie movie = new Movie("Test Movie", "Test Description", 9.99, new MovieGenre("Action"), "test-image.jpg", actors);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        boolean result = movieService.deleteMovie(movieId);

        assertTrue(result);
        verify(movieRepository, times(1)).delete(movie);
    }

    @Test
    @DisplayName("Should delete the movie when the movieId is valid")
    void deleteMovieWhenMovieIdIsValid() {
        Long movieId = 1L;
        List<Actor> actors = Arrays.asList(
                new Actor("Actor 1"),
                new Actor("Actor 2")
        );
        Movie movie = new Movie("Test Movie", "Test Description", 9.99, new MovieGenre("Action"), "test-image.jpg", actors);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        boolean result = movieService.deleteMovie(movieId);

        assertTrue(result);
        verify(movieRepository, times(1)).delete(movie);
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    @DisplayName("Should throw an exception when the movieId is invalid")
    void deleteMovieWhenMovieIdIsInvalidThenThrowException() {
        Long movieId = 1L;
        List<Actor> actors = Arrays.asList(
                new Actor("Actor 1"),
                new Actor("Actor 2")
        );
        Movie movie = new Movie("Test Movie", "Test Description", 9.99, new MovieGenre("Action"), "test-image.jpg", actors);

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(MovieNotFound.class,
                () -> movieService.deleteMovie(movieId)
        );

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).delete(any(Movie.class));
    }

    @Test
    @DisplayName("Should throw an exception when the movie genre is not found")
    void findAllMoviesFromMovieGenreWhenMovieGenreNotFoundThenThrowException() {
        Long movieGenreId = 1L;
        MovieGenre movieGenre = new MovieGenre("Action");
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.empty());

        assertThrows(MovieGenreNotFound.class, () -> {
            movieService.findAllMoviesFromMovieGenre(movieGenreId);
        });

        verify(movieGenreRepository, times(1)).findById(movieGenreId);
        verifyNoMoreInteractions(movieRepository, actorRepository, movieGenreRepository);
    }

    @Test
    @DisplayName("Should return all movies from a specific movie genre")
    void findAllMoviesFromMovieGenre() {
        Long movieGenreId = 1L;
        MovieGenre movieGenre = new MovieGenre("Action");
        List<Movie> movies = Arrays.asList(
                new Movie("Movie 1", "Description 1", 10.0, movieGenre, "image1.jpg", null),
                new Movie("Movie 2", "Description 2", 15.0, movieGenre, "image2.jpg", null),
                new Movie("Movie 3", "Description 3", 20.0, movieGenre, "image3.jpg", null)
        );

        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(movieGenre));
        when(movieRepository.findAllByMovieGenre(movieGenre)).thenReturn(movies);

        List<Movie> result = movieService.findAllMoviesFromMovieGenre(movieGenreId);

        assertEquals(movies.size(), result.size());
        assertEquals(movies, result);

        verify(movieGenreRepository, times(1)).findById(movieGenreId);
        verify(movieRepository, times(1)).findAllByMovieGenre(movieGenre);
    }

    @Test
    @DisplayName("Should throw an exception when the provided movieGenreId does not exist")
    void updateMovieWhenMovieGenreIdDoesNotExistThenThrowException() {
        Long movieId = 1L;
        String name = "Updated Movie";
        String description = "Updated description";
        double price = 9.99;
        Long movieGenreId = 2L;
        String imageUrl = "https://example.com/image.jpg";
        List<Long> actorsId = Arrays.asList(3L, 4L);

        MovieGenre movieGenre = new MovieGenre("Action");
        List<Actor> actors = Arrays.asList(
                new Actor("Actor 1"),
                new Actor("Actor 2")
        );

        Movie movie = new Movie("Movie", "Description", 9.99, movieGenre, "https://example.com/image.jpg", actors);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(MovieGenreNotFound.class, () -> {
            movieService.updateMovie(movieId, name, description, price, movieGenreId, imageUrl, actorsId);
        });

        verify(movieRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw an exception when the provided id does not exist")
    void updateMovieWhenIdDoesNotExistThenThrowException() {
        Long id = 1L;
        String name = "Updated Movie";
        String description = "Updated description";
        double price = 9.99;
        Long movieGenreId = 2L;
        String imageUrl = "https://example.com/image.jpg";
        List<Long> actorsId = Arrays.asList(3L, 4L);

        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(MovieNotFound.class, () -> {
            movieService.updateMovie(id, name, description, price, movieGenreId, imageUrl, actorsId);
        });

        // Verify
        verify(movieRepository, times(1)).findById(id);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    @DisplayName("Should throw an exception when the provided actorsId does not exist")
    void updateMovieWhenActorsIdDoesNotExistThenThrowException() {
        Long movieId = 1L;
        String name = "Updated Movie";
        String description = "Updated description";
        double price = 9.99;
        Long movieGenreId = 2L;
        String imageUrl = "https://example.com/image.jpg";
        List<Long> actorsId = Arrays.asList(3L, 4L);

        MovieGenre movieGenre = new MovieGenre("Action");
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(movieGenre));

        Movie movie = new Movie("Old Movie", "Old description", 5.99, movieGenre, "https://example.com/old-image.jpg", Arrays.asList(new Actor("Actor 1"), new Actor("Actor 2")));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        List<Actor> actors = Arrays.asList(new Actor("Actor 3"), new Actor("Actor 4"));
        when(actorRepository.findAllById(actorsId)).thenReturn(actors);

        Optional<Movie> updatedMovie = movieService.updateMovie(movieId, name, description, price, movieGenreId, imageUrl, actorsId);

        assertTrue(updatedMovie.isPresent());
        assertEquals(name, updatedMovie.get().getName());
        assertEquals(description, updatedMovie.get().getDescription());
        assertEquals(price, updatedMovie.get().getPrice());
        assertEquals(movieGenre, updatedMovie.get().getMovieGenre());
        assertEquals(imageUrl, updatedMovie.get().getImageUrl());
        assertEquals(actors, updatedMovie.get().getActors());

        verify(movieGenreRepository, times(1)).findById(movieGenreId);
        verify(movieRepository, times(1)).findById(movieId);
        verify(actorRepository, times(1)).findAllById(actorsId);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    @DisplayName("Should update the movie when valid id, name, description, price, movieGenreId, imageUrl, and actorsId are provided")
    void updateMovieWithValidInputs() {
        Long movieId = 1L;
        String name = "Updated Movie";
        String description = "Updated description";
        double price = 9.99;
        Long movieGenreId = 2L;
        String imageUrl = "https://example.com/image.jpg";
        List<Long> actorsId = Arrays.asList(3L, 4L);

        MovieGenre movieGenre = new MovieGenre("Action");
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(movieGenre));

        Actor actor1 = new Actor("Actor 1");
        Actor actor2 = new Actor("Actor 2");
        when(actorRepository.findAllById(actorsId)).thenReturn(Arrays.asList(actor1, actor2));

        Movie existingMovie = new Movie("Old Movie", "Old description", 5.99, movieGenre, "https://example.com/old-image.jpg", Arrays.asList(actor1));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        Optional<Movie> updatedMovie = movieService.updateMovie(movieId, name, description, price, movieGenreId, imageUrl, actorsId);

        assertTrue(updatedMovie.isPresent());
        assertEquals(name, updatedMovie.get().getName());
        assertEquals(description, updatedMovie.get().getDescription());
        assertEquals(price, updatedMovie.get().getPrice());
        assertEquals(movieGenre, updatedMovie.get().getMovieGenre());
        assertEquals(imageUrl, updatedMovie.get().getImageUrl());
        assertEquals(2, updatedMovie.get().getActors().size());
        assertTrue(updatedMovie.get().getActors().contains(actor1));
        assertTrue(updatedMovie.get().getActors().contains(actor2));

        verify(movieGenreRepository, times(1)).findById(movieGenreId);
        verify(actorRepository, times(1)).findAllById(actorsId);
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    @DisplayName("Should throw an exception when the id does not exist")
    void getMovieWhenIdDoesNotExistThenThrowException() {
        Long id = 1L;
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovieNotFound.class, () -> movieService.getMovie(id));

        verify(movieRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should return the movie when the id exists")
    void getMovieWhenIdExists() {
        Long movieId = 1L;
        String newName = "John Doe";
        Actor existingActor = new Actor("Jane Smith");
        Movie movie = new Movie("Movie 1", "Description 1", 9.99, new MovieGenre("Genre 1"), "image.jpg",
                Arrays.asList(existingActor));

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovie(movieId);

        assertEquals(movie, result);
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    @DisplayName("Should throw an exception when the movie genre is not found")
    void addNewMovieWhenMovieGenreNotFoundThenThrowException() {
        String name = "Avengers: Endgame";
        String description = "The epic conclusion to the Infinity Saga.";
        double price = 19.99;
        Long movieGenreId = 1L;
        String imageUrl = "https://example.com/avengers-endgame.jpg";
        List<Long> actorsId = Arrays.asList(1L, 2L, 3L);

        MovieGenre movieGenre = new MovieGenre("Action");
        List<Actor> actors = Arrays.asList(
                new Actor("Robert Downey Jr."),
                new Actor("Chris Evans"),
                new Actor("Scarlett Johansson")
        );

        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.empty());

        assertThrows(MovieGenreNotFound.class, () -> {
            movieService.addNewMovie(name, description, price, movieGenreId, imageUrl, actorsId);
        });

        verify(movieGenreRepository, times(1)).findById(movieGenreId);
        verifyNoMoreInteractions(movieRepository, actorRepository, movieGenreRepository);
    }

    @Test
    @DisplayName("Should throw an exception when any of the actors is not found")
    void addNewMovieWhenActorNotFoundThenThrowException() {
        String name = "Test Movie";
        String description = "This is a test movie";
        double price = 9.99;
        Long movieGenreId = 1L;
        String imageUrl = "https://example.com/test-movie.jpg";
        List<Long> actorsId = Arrays.asList(1L, 2L, 3L);

        MovieGenre movieGenre = new MovieGenre("Action");
        List<Actor> actors = Arrays.asList(
                new Actor("Actor 1"),
                new Actor("Actor 2"),
                new Actor("Actor 3")
        );

        when(actorRepository.findAllById(actorsId)).thenReturn(actors);
        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(movieGenre));

        assertThrows(MovieGenreNotFound.class, () -> {
            movieService.addNewMovie(name, description, price, movieGenreId, imageUrl, actorsId);
        });

        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    @DisplayName("Should return the saved movie when the movie is successfully added")
    void addNewMovieReturnsSavedMovieWhenSuccessfullyAdded() {
        String name = "Avengers: Endgame";
        String description = "The epic conclusion to the Infinity Saga.";
        double price = 19.99;
        Long movieGenreId = 1L;
        String imageUrl = "https://example.com/avengers-endgame.jpg";
        List<Long> actorsId = Arrays.asList(1L, 2L, 3L);

        MovieGenre movieGenre = new MovieGenre("Action");
        List<Actor> actors = Arrays.asList(
                new Actor("Robert Downey Jr."),
                new Actor("Chris Evans"),
                new Actor("Scarlett Johansson")
        );

        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(movieGenre));
        when(actorRepository.findAllById(actorsId)).thenReturn(actors);

        Movie savedMovie = new Movie(name, description, price, movieGenre, imageUrl, actors);
        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        Optional<Movie> result = movieService.addNewMovie(name, description, price, movieGenreId, imageUrl, actorsId);

        assertTrue(result.isPresent());
        assertEquals(savedMovie, result.get());

        verify(movieGenreRepository, times(1)).findById(movieGenreId);
        verify(actorRepository, times(1)).findAllById(actorsId);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    @DisplayName("Should add a new movie when all parameters are valid")
    void addNewMovieWhenAllParametersAreValid() {
        String name = "Avengers: Endgame";
        String description = "The epic conclusion to the Infinity Saga.";
        double price = 19.99;
        Long movieGenreId = 1L;
        String imageUrl = "https://example.com/avengers-endgame.jpg";
        List<Long> actorsId = Arrays.asList(1L, 2L, 3L);

        MovieGenre movieGenre = new MovieGenre("Action");
        List<Actor> actors = Arrays.asList(
                new Actor("Robert Downey Jr."),
                new Actor("Chris Evans"),
                new Actor("Scarlett Johansson")
        );

        when(movieGenreRepository.findById(movieGenreId)).thenReturn(Optional.of(movieGenre));
        when(actorRepository.findAllById(actorsId)).thenReturn(actors);

        Optional<Movie> result = movieService.addNewMovie(name, description, price, movieGenreId, imageUrl, actorsId);

        assertTrue(result.isPresent());
        Movie movie = result.get();
        assertEquals(name, movie.getName());
        assertEquals(description, movie.getDescription());
        assertEquals(price, movie.getPrice());
        assertEquals(movieGenre, movie.getMovieGenre());
        assertEquals(imageUrl, movie.getImageUrl());
        assertEquals(actors, movie.getActors());

        verify(movieRepository, times(1)).save(movie);
    }
}