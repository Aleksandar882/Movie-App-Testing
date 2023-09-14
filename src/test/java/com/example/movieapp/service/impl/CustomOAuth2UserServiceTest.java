package com.example.movieapp.service.impl;

import com.example.movieapp.model.CustomOAuth2User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    @DisplayName("Should throw an OAuth2AuthenticationException when the user cannot be loaded")
    void loadUserThrowsOAuth2AuthenticationExceptionWhenUserCannotBeLoaded() {
        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        OAuth2AuthenticationException exception = mock(OAuth2AuthenticationException.class);

        when(delegate.loadUser(userRequest)).thenThrow(exception);

        assertThrows(OAuth2AuthenticationException.class, () -> {
            customOAuth2UserService.loadUser(userRequest);
        });

        verify(delegate, times(1)).loadUser(userRequest);
    }

    @Test
    @DisplayName("Should load the user and return a CustomOAuth2User")
    void loadUserReturnsCustomOAuth2User() {
        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        OAuth2User oauth2User = mock(OAuth2User.class);
        CustomOAuth2User customOAuth2User = mock(CustomOAuth2User.class);

        when(delegate.loadUser(userRequest)).thenReturn(oauth2User);
        when(customOAuth2User.getEmail()).thenReturn("test@example.com");

        OAuth2User result = customOAuth2UserService.loadUser(userRequest);

        assertThat(result).isInstanceOf(CustomOAuth2User.class);
        assertThat(result.getAttributes()).isEqualTo(oauth2User.getAttributes());
        assertThat(result.getAuthorities()).isEqualTo(oauth2User.getAuthorities());
        assertThat(result.getName()).isEqualTo(oauth2User.getAttribute("name"));
        assertThat(customOAuth2User.getEmail()).isEqualTo("test@example.com");

        verify(delegate, times(1)).loadUser(userRequest);
    }

}