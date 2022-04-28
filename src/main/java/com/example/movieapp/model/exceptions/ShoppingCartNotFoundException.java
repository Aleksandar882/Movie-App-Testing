package com.example.movieapp.model.exceptions;

public class ShoppingCartNotFoundException extends RuntimeException{

    public ShoppingCartNotFoundException(Long id) {
        super(String.format("Shopping cart with id: %d was not found", id));
    }

}
