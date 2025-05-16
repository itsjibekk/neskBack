package org.example.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.example.config.UserAuthProvider;
import org.example.dto.*;
import org.example.entity.User;
import org.example.exception.AppException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto){
        UserDto user = userService.login(credentialsDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto){
        UserDto user = userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (userAuthProvider.validateRefreshToken(refreshToken)) {
            DecodedJWT decoded = JWT.decode(refreshToken);
            User user = userRepository.findByLogin(decoded.getIssuer())
                    .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
            UserDto userDto = userMapper.toUserDto(user);
            String newAccessToken = userAuthProvider.createToken(userDto);
            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        }
        throw new AppException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
    }
}
