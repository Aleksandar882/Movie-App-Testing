package com.example.movieapp.service.impl;

import com.example.movieapp.model.User;
import com.example.movieapp.model.exceptions.InvalidArgumentsException;
import com.example.movieapp.model.exceptions.InvalidUserCredentialsException;
import com.example.movieapp.repository.UserRepository;
import com.example.movieapp.service.AuthService;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username==null || username.isEmpty() || password==null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidUserCredentialsException::new);
    }
}
