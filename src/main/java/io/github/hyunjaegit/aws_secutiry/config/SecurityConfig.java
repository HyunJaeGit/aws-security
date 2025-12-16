package io.github.hyunjaegit.aws_secutiry.config;

import io.github.hyunjaegit.aws_secutiry.user.User;
import io.github.hyunjaegit.aws_secutiry.filter.JwtAuthenticationFilter; // import 추가
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // import 추가


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. 필드 선언 (클래스 레벨)
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 2. 생성자 추가 (클래스 레벨, DI 받음)
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 방어 비활성화 (JWT는 세션을 사용하지 않으므로)

                .authorizeHttpRequests(authorize -> authorize
                        // 3. 로그인 API만 인증 없이 접근 허용
                        .requestMatchers("/api/auth/login").permitAll()

                        // /api/logs와 그 외 모든 요청은 인증 필요
                        .requestMatchers("/api/logs").authenticated()
                        .anyRequest().authenticated()
                )
                // 4. 폼 로그인 및 기본 HTTP 인증 비활성화 (JWT를 사용하므로)
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        // 5. 커스텀 JWT 필터를 스프링 기본 인증 필터보다 먼저 동작하도록 등록
        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        User tempUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("qwer1234"))
                .authorities(
                        java.util.List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                )
                .build();
        return new InMemoryUserDetailsManager(tempUser);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}