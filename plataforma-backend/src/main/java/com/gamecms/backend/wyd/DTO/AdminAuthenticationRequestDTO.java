package com.gamecms.backend.wyd.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gamecms.backend.wyd.DTO.AbstractDTO.AbstractRecaptchaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAuthenticationRequestDTO extends AbstractRecaptchaDTO implements UserDetails {
    private String email;
    private String password;

    @JsonIgnore
    private boolean credentialsNonExpired;
    @JsonIgnore
    private boolean accountNonExpired;
    @JsonIgnore
    private boolean accountNonLocked;

    @JsonIgnore
    private List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADMIN"));

    public AdminAuthenticationRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

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
