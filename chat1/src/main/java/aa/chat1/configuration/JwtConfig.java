package aa.chat1.configuration;

import aa.chat1.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public JwtFilter jwtFilter(UserService userService) {
        return new JwtFilter(userService, secretKey);
    }
}
