package com.example.movieapp.repository;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.ShoppingCart;
import com.example.movieapp.model.ShoppingCartStatus;
import com.example.movieapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

    Optional<ShoppingCart> findByUserAndStatus(User user, ShoppingCartStatus status);

}
