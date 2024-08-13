package firstdecision.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        
        Cookie cookie = new Cookie("JWT", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); 
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
