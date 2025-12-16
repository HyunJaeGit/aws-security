package io.github.hyunjaegit.aws_secutiry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ⚠️ 이 부분은 그대로 두셔도 됩니다.
                // 아래의 authorizeHttpRequests 부분만 변경할게요.

                // 변경 전: .authorizeHttpRequests(authorize -> authorize...)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/logs").permitAll()
                        .anyRequest().authenticated()
                )
        // .formLogin(Customizer.withDefaults()) // 기본 로그인 폼 사용 (필요 시)
        ;

        return http.build();
    }
}