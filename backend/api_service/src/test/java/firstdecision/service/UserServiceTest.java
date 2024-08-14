package firstdecision.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import firstdecision.model.User;
import firstdecision.repository.UserRepository;
import firstdecision.util.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserFromAuthentication_WhenOAuth2User_ThenReturnUser() {
        // Arrange
        OAuth2User oAuth2User = new DefaultOAuth2User(Collections.emptySet(), Collections.singletonMap("email", "user@example.com"), "email");
        when(authentication.getPrincipal()).thenReturn(oAuth2User);

        User expectedUser = new User();
        expectedUser.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.getUserFromAuthentication(authentication);

        // Assert
        assertNotNull(actualUser);
        assertEquals("user@example.com", actualUser.getEmail());
    }

    @Test
    void getUserFromAuthentication_WhenNonOAuth2User_ThenThrowException() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(new Object()); // Non-OAuth2User principal

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.getUserFromAuthentication(authentication));
    }

    @Test
    void getUserFromAuthentication_WhenUserNotFound_ThenThrowException() {
        // Arrange
        OAuth2User oAuth2User = new DefaultOAuth2User(Collections.emptySet(), Collections.singletonMap("email", "unknown@example.com"), "email");
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.getUserFromAuthentication(authentication));
    }
}
