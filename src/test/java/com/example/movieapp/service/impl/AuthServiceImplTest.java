package com.example.movieapp.service.impl;

import com.example.movieapp.model.Role;
import com.example.movieapp.model.User;
import com.example.movieapp.model.exceptions.InvalidArgumentsException;
import com.example.movieapp.model.exceptions.InvalidUserCredentialsException;
import com.example.movieapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Should throw InvalidUserCredentialsException when username and password do not match any user")
    void loginWhenUsernameAndPasswordDoNotMatchThenThrowInvalidUserCredentialsException() {
        String username = "john.doe";
        String password = "password123";

        when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.empty());

        assertThrows(InvalidUserCredentialsException.class, () -> authService.login(username, password));

        verify(userRepository, times(1)).findByUsernameAndPassword(username, password);
    }

    @Test
    @DisplayName("Should return the user when username and password match an existing user")
    void loginWhenUsernameAndPasswordMatchThenReturnUser() {
        String username = "john.doe";
        String password = "password123";

        User user = new User(username, "john.doe@example.com", password, Role.ROLE_USER);

        when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(user));

        User result = authService.login(username, password);

        assertEquals(user, result);
        verify(userRepository, times(1)).findByUsernameAndPassword(username, password);
    }

    @Test
    @DisplayName("Should throw InvalidArgumentsException when username or password is null or empty")
    void loginWhenUsernameOrPasswordIsEmptyOrNullThenThrowInvalidArgumentsException() {
        String username = null;
        String password = "password";

        String finalUsername = username;
        String finalPassword = password;
        assertThrows(InvalidArgumentsException.class, () -> {
            authService.login(finalUsername, finalPassword);
        });

        username = "";
        password = "password";

        String finalUsername1 = username;
        String finalPassword1 = password;
        assertThrows(InvalidArgumentsException.class, () -> {
            authService.login(finalUsername1, finalPassword1);
        });

        username = "username";
        password = null;

        String finalUsername2 = username;
        String finalPassword2 = password;
        assertThrows(InvalidArgumentsException.class, () -> {
            authService.login(finalUsername2, finalPassword2);
        });

        username = "username";
        password = "";

        String finalUsername3 = username;
        String finalPassword3 = password;
        assertThrows(InvalidArgumentsException.class, () -> {
            authService.login(finalUsername3, finalPassword3);
        });

        verifyNoInteractions(userRepository);
    }

}