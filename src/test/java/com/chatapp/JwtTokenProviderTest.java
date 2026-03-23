package com.chatapp;

import com.chatapp.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void generateAccessToken_shouldReturnValidToken() {
        UserDetails userDetails = new User(
                "test@example.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtTokenProvider.generateAccessToken(userDetails);

        assertThat(token).isNotNull().isNotEmpty();
        assertThat(jwtTokenProvider.extractUsername(token)).isEqualTo("test@example.com");
        assertThat(jwtTokenProvider.isTokenValid(token, userDetails)).isTrue();
    }
}
