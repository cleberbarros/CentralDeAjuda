package firstdecision.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import firstdecision.service.ProcessUserLogin;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final ProcessUserLogin processUserLogin;

    @Autowired
    public CustomOAuth2UserService(ProcessUserLogin processUserLogin) {
        this.processUserLogin = processUserLogin;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        processUserLogin.execute(oAuth2User, response);
        return oAuth2User;
    }
}
