package com.example.movieapp.model.exceptions;

public class MovieAlreadyInShoppingCartException extends RuntimeException {

    public MovieAlreadyInShoppingCartException(Long id, String username) {
        super(String.format("Movie with id: %d already exists in the shopping cart for the user with username %s", id, username));
    }
}
