package com.example.movieapp.service;

import com.example.movieapp.model.User;

public interface AuthService {

    User login(String username, String password);

}
