package com.example.movieapp.service;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCart deleteMovieFromShoppingCart(String username, Long movieId);

    public void deleteCart(String username);

    double getPrice(Long cartId);

    List<Movie> listAllMoviesInShoppingCart(Long cartId);

    ShoppingCart getActiveShoppingCart(String username);

    ShoppingCart addMovieToShoppingCart(String username, Long movieId);
}
