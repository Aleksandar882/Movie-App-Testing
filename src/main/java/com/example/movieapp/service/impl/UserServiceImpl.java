package com.example.movieapp.service.impl;

import com.example.movieapp.model.Provider;
import com.example.movieapp.model.Role;
import com.example.movieapp.model.User;
import com.example.movieapp.model.exceptions.InvalidUsernameOrPasswordException;
import com.example.movieapp.model.exceptions.PasswordsDoNotMatchException;
import com.example.movieapp.model.exceptions.UsernameAlreadyExistsException;
import com.example.movieapp.repository.UserRepository;
import com.example.movieapp.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

      private final UserRepository userRepository;
      private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username, String email, String password,String repeatPassword, Role role) {
        if (username==null || username.isEmpty()  || password==null || password.isEmpty())
            throw new InvalidUsernameOrPasswordException();
        if (!password.equals(repeatPassword))
            throw new PasswordsDoNotMatchException();
        if(this.userRepository.findByUsername(username).isPresent())
            throw new UsernameAlreadyExistsException(username);
        User user = new User(username,email,passwordEncoder.encode(password),role);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s).orElseThrow(()->new UsernameNotFoundException(s));
    }

    @Override
    public void processOAuthPostLogin(String username, String email) {
        User existUser = userRepository.getUserByUsername(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setRole(Role.ROLE_USER);
            userRepository.save(newUser);
        }

    }
}
