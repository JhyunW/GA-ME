package ssafy.ggame.domain.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // /user/login 경로에 대한 요청은 누구나 접근할 수 있도록 설정합니다.
                        //.requestMatchers("/user/login").permitAll()
                        // 나머지 모든 요청은 인증된 사용자만 접근할 수 있도록 설정합니다.
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
