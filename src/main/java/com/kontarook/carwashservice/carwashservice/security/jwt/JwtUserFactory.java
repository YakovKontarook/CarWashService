package com.kontarook.carwashservice.carwashservice.security.jwt;

import com.kontarook.carwashservice.carwashservice.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    public static JwtUser create(Integer id, String username, List<String> roles, String email) {
        return new JwtUser(
                id, username, email,
                mapToGrantedAuthorities(roles)
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Iterable<?> userRoles) {
        return StreamSupport.stream(userRoles.spliterator(), false)
                .map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
    }
}
