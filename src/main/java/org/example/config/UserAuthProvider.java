package org.example.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.dto.RoleDto;
import org.example.entity.User;
import org.example.exception.AppException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;

import org.example.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAuthProvider {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

     @PostConstruct
     protected void init(){
         secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
     }

    public String createToken(UserDto dto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 86_400_000); // 24 часа

        return JWT.create()
                .withIssuer(dto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("roles", dto.getRoles().stream()
                        .map(role -> "ROLE_" + role.getName()) // Добавьте префикс
                        .collect(Collectors.toList()))
                .withClaim("firstName", dto.getFirstName())
                .withClaim("lastName", dto.getLastName())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        List<String> roles = decoded.getClaim("roles").asList(String.class);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDto user = UserDto.builder()
                .login(decoded.getIssuer())
                .firstName(decoded.getClaim("firstName").asString())
                .lastName(decoded.getClaim("lastName").asString())
                .build();

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);
        User user = userRepository.findByLoginWithRoles(decoded.getIssuer()) // Use the new method
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        UserDto userDto = userMapper.toUserDto(user);
        List<SimpleGrantedAuthority> authorities = userDto.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        System.out.println("Authorities: " + authorities);
        return new UsernamePasswordAuthenticationToken(
                userDto,
                null,
                authorities // Добавляем роли как authorities
        );
    }
    public String createRefreshToken(UserDto dto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 7 * 86_400_000); // 7 days
        return JWT.create()
                .withIssuer(dto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("refresh", true) // Mark as refresh token
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("refresh", true)
                    .build();
            DecodedJWT decoded = verifier.verify(refreshToken);
            return userRepository.findByLogin(decoded.getIssuer()).isPresent();
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
