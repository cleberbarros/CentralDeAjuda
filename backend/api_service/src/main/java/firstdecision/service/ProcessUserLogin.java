package firstdecision.service;

import firstdecision.model.Role;
import firstdecision.model.User;
import firstdecision.repository.UserRepository;
import firstdecision.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProcessUserLogin {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public void execute(OAuth2User oAuth2User, HttpServletResponse response) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER);
            newUser.setRoles(roles);
            return userRepository.save(newUser);
        });

        String token = jwtTokenProvider.generateToken(user.getEmail());

        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
