package com.example.movieapp.service.impl;

import com.example.movieapp.model.*;
import com.example.movieapp.model.exceptions.MovieAlreadyInShoppingCartException;
import com.example.movieapp.model.exceptions.MovieNotFound;
import com.example.movieapp.model.exceptions.ShoppingCartNotFoundException;
import com.example.movieapp.model.exceptions.UserNotFoundException;
import com.example.movieapp.repository.MovieRepository;
import com.example.movieapp.repository.ShoppingCartRepository;
import com.example.movieapp.repository.UserRepository;
import com.example.movieapp.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(UserRepository userRepository, MovieRepository movieRepository, ShoppingCartRepository shoppingCartRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public List<Movie> listAllMoviesInShoppingCart(Long cartId) {
        if(!this.shoppingCartRepository.findById(cartId).isPresent())
            throw new ShoppingCartNotFoundException(cartId);
        List<Movie> movies=this.shoppingCartRepository.findById(cartId).get().getMovies();
       movies.forEach(Movie::getPrice);
        return this.shoppingCartRepository.findById(cartId).get().getMovies();
    }

    @Override
    public double getPrice(Long cartId) {
        if(!this.shoppingCartRepository.findById(cartId).isPresent())
            throw new ShoppingCartNotFoundException(cartId);
        List<Movie> movies=this.shoppingCartRepository.findById(cartId).get().getMovies();
          List<Double> pricesList= movies.stream().map(Movie::getPrice).collect(Collectors.toList());
        double sum = 0.0;
        for (int i = 0; i < pricesList.size(); i++)
            sum += pricesList.get(i);
        return sum;
    }
    @Override
    public void deleteCart(String username){
        ShoppingCart shoppingCart=this.getActiveShoppingCart(username);
        this.shoppingCartRepository.delete(shoppingCart);
    }

    

    @Override
    public ShoppingCart getActiveShoppingCart(String username) {

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return this.shoppingCartRepository
                .findByUserAndStatus(user, ShoppingCartStatus.CREATED)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart(user);
                    return this.shoppingCartRepository.save(cart);
                });
    }

    @Override
    public ShoppingCart addMovieToShoppingCart(String username, Long movieId) {

        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        Movie movie = this.movieRepository.findById(movieId).orElseThrow(MovieNotFound::new);

        if(shoppingCart.getMovies()
                .stream().filter(i -> i.getId().equals(movieId))
                .collect(Collectors.toList()).size() > 0)
            throw new MovieAlreadyInShoppingCartException(movieId, username);
        shoppingCart.getMovies().add(movie);
        return this.shoppingCartRepository.save(shoppingCart);

    }

    @Override
    public ShoppingCart deleteMovieFromShoppingCart(String username, Long movieId) {

        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        Movie movie = this.movieRepository.findById(movieId).orElseThrow(MovieNotFound::new);
        shoppingCart.getMovies().remove(movie);
        return this.shoppingCartRepository.save(shoppingCart);

    }
}
