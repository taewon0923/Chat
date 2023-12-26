package aa.chat1.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // 여기서 "/api"는 프록시 설정과 일치해야 합니다.
                .allowedOrigins("http://localhost:3000") // 허용할 오리진을 설정합니다.
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드를 설정합니다.
                .allowCredentials(true); // 인증 정보를 허용할지 여부를 설정합니다.
    }
}
