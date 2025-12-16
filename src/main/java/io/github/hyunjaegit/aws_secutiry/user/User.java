package io.github.hyunjaegit.aws_secutiry.user;


import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@Getter
public class User implements UserDetails {

    private final String username; // 사용자 ID
    private final String password; // 비밀번호
    private final Collection<? extends GrantedAuthority> authorities; // 권한 목록

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // 계정 상태 관련 설정 (모두 true로 설정하여 항상 사용 가능한 상태로 둠)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
