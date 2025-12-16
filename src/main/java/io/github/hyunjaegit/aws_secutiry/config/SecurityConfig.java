package io.github.hyunjaegit.aws_secutiry.config;

import io.github.hyunjaegit.aws_secutiry.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호를 안전하게 암호화(해시) 해주는 BCrypt 알고리즘 사용
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // 임시 사용자 정보를 메모리에 등록합니다.

        // **중요:** 사용자 비밀번호 "password"는 반드시 인코딩되어 저장되어야 합니다!
        User tempUser = User.builder()
                .username("admin") // 로그인 ID
                .password(passwordEncoder.encode("qwer1234")) // 비밀번호를 암호화하여 저장
                .authorities(
                        // 권한은 임시로 ROLE_ADMIN 하나를 부여
                        java.util.List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                )
                .build();

        // 메모리 기반 UserDetailsManager에 사용자 정보를 등록
        return new InMemoryUserDetailsManager(tempUser);
    }
}