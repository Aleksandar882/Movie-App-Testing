package com.example.movieapp.service.impl;

import com.example.movieapp.model.*;
import com.example.movieapp.model.exceptions.ShoppingCartNotFoundException;
import com.example.movieapp.repository.MovieRepository;
import com.example.movieapp.repository.ShoppingCartRepository;
import com.example.movieapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User user;
    private MovieGenre movieGenre;
    private List<Actor> actors;

    @BeforeEach
    void setUp() {
        user = new User("user", "password", "email@email.com", Role.ROLE_USER);
        movieGenre = new MovieGenre("genre");
        actors = new ArrayList<>();
        actors.add(new Actor("actor1"));
        actors.add(new Actor("actor2"));
    }


    @Test
    @DisplayName("Should delete the shopping cart for a given username")
    void deleteCartForGivenUsername() {
        String username = "user";
        ShoppingCart shoppingCart = new ShoppingCart(user);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserAndStatus(user, ShoppingCartStatus.CREATED)).thenReturn(Optional.of(shoppingCart));

        shoppingCartService.deleteCart(username);

        verify(shoppingCartRepository, times(1)).delete(shoppingCart);
    }

    @Test
    @DisplayName("Should throw an exception when trying to delete a non-existing shopping cart")
    void deleteCartWhenCartDoesNotExistThenThrowException() {
        String username = "user";
        ShoppingCart shoppingCart = new ShoppingCart(user);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserAndStatus(user, ShoppingCartStatus.CREATED)).thenReturn(Optional.empty());

        assertThrows(ShoppingCartNotFoundException.class, () -> {
            shoppingCartService.deleteCart(username);
        });

        verify(userRepository, times(1)).findByUsername(username);
        verify(shoppingCartRepository, times(1)).findByUserAndStatus(user, ShoppingCartStatus.CREATED);
        verify(shoppingCartRepository, never()).delete(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Should throw ShoppingCartNotFoundException when the cart id is not valid")
    void listAllMoviesInShoppingCartWhenCartIdIsNotValidThenThrowException() {
        Long cartId = 1L;
        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(ShoppingCartNotFoundException.class, () -> {
            shoppingCartService.listAllMoviesInShoppingCart(cartId);
        });

        verify(shoppingCartRepository, times(1)).findById(cartId);
    }

    @Test
    @DisplayName("Should return all movies in the shopping cart when the cart id is valid")
    void listAllMoviesInShoppingCartWhenCartIdIsValid() {
        Long cartId = 1L;
        ShoppingCart shoppingCart = new ShoppingCart(user);
        shoppingCart.setId(cartId);
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Movie 1", "Description 1", 10.0, movieGenre, "image1.jpg", actors));
        movies.add(new Movie("Movie 2", "Description 2", 15.0, movieGenre, "image2.jpg", actors));
        shoppingCart.setMovies(movies);

        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(shoppingCart));

        List<Movie> result = shoppingCartService.listAllMoviesInShoppingCart(cartId);

        assertEquals(movies, result);
        verify(shoppingCartRepository, times(1)).findById(cartId);
    }
}