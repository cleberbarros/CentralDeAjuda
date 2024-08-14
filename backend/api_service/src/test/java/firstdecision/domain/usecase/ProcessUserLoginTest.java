package firstdecision.domain.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import firstdecision.model.Role;
import firstdecision.model.User;
import firstdecision.repository.UserRepository;
import firstdecision.util.JwtTokenProvider;
import firstdecision.service.ProcessUserLogin;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ProcessUserLoginTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ProcessUserLogin processUserLogin;

    @Captor
    private ArgumentCaptor<Cookie> cookieCaptor;

    private OAuth2User oAuth2User;
    private User user;
    private String userEmail = "test@example.com";
    private String userName = "Test User";
    private String userToken = "userToken";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail(userEmail);
        user.setName(userName);
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        Map<String, Object> attributes = Map.of("email", userEmail, "name", userName);
        oAuth2User = new DefaultOAuth2User(Collections.emptyList(), attributes, "email");

        when(jwtTokenProvider.generateToken(userEmail)).thenReturn(userToken);
    }

    @Test
    void execute_ShouldCreateUserIfNotExists() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user); // Make sure to return the newly created user

        processUserLogin.execute(oAuth2User, response);

        verify(userRepository, times(1)).save(any(User.class));
        verify(response, times(1)).addCookie(cookieCaptor.capture());

        // Verify the properties of the added cookie
        Cookie cookie = cookieCaptor.getValue();
        assertEquals("JWT", cookie.getName());
        assertEquals(userToken, cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }

    @Test
    void execute_ShouldUseExistingUserIfExists() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        processUserLogin.execute(oAuth2User, response);

        verify(userRepository, never()).save(any(User.class));
        verify(response, times(1)).addCookie(cookieCaptor.capture());
        assertEquals("JWT", cookieCaptor.getValue().getName());
        assertEquals(userToken, cookieCaptor.getValue().getValue());
        assertTrue(cookieCaptor.getValue().isHttpOnly());
        assertEquals("/", cookieCaptor.getValue().getPath());
    }

    @Test
    void execute_ShouldSetCookieForToken() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        processUserLogin.execute(oAuth2User, response);

        verify(response, times(1)).addCookie(cookieCaptor.capture());
        Cookie cookie = cookieCaptor.getValue();
        assertEquals("JWT", cookie.getName());
        assertEquals(userToken, cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertEquals("/", cookie.getPath());
    }
}
