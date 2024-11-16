package org.example.cookbook.service;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.user.*;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.model.enums.Role;
import org.example.cookbook.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    public RegisterResponse registerUser(RegisterForm registerForm) {
        if (this.userRepository.findUserByEmail(registerForm.getEmail()).isPresent()) {
            return new RegisterResponse(null, HttpStatus.CONFLICT);
        }

        UserEntity user = new UserEntity()
                .setEmail(registerForm.getEmail())
                .setFirstName(registerForm.getFirstName())
                .setLastName(registerForm.getLastName())
                .setPassword(passwordEncoder.encode(registerForm.getPassword()))
                .setRole(Role.USER);

        UserDto dto = modelMapper.map(this.userRepository.save(user), UserDto.class);

        return new RegisterResponse(dto, HttpStatus.CREATED);
    }

    public LoginResponse login(LoginForm loginForm) {
        Authentication authentication;

        try {
            authentication = this.authenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        } catch (AuthenticationException e) {
            return new LoginResponse(null, HttpStatus.UNAUTHORIZED, "");
        }

        final UserEntity user = this.userRepository.findUserByEmail(loginForm.getEmail()).orElseThrow();

        final String token = this.jwtService.generateToken(loginForm.getEmail(), Map.of("role", user.getRole().name()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new LoginResponse(modelMapper.map(user, UserDto.class), HttpStatus.OK, token);

    }

    public UserEntity getCurrentLoggedInUser() {
        return this.userRepository.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElse(null);
    }
}
