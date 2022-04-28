package com.example.movieapp.service;

import com.example.movieapp.model.Role;
import com.example.movieapp.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void processOAuthPostLogin(String username, String email);

    User register(String username, String email, String password, String repeatPassword, Role role);

}
