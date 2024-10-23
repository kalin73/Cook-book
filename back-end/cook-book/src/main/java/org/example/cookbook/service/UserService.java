package org.example.cookbook.service;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.user.*;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public RegisterResponse registerUser(RegisterForm registerForm) {
        if (this.userRepository.findUserByEmail(registerForm.getEmail()).isPresent()) {
            return new RegisterResponse(null, HttpStatus.CONFLICT);
        }

        UserEntity user = new UserEntity()
                .setEmail(registerForm.getEmail())
                .setFirstName(registerForm.getFirstName())
                .setLastName(registerForm.getLastName())
                .setPassword(passwordEncoder.encode(registerForm.getPassword()));

        UserDto dto = modelMapper.map(this.userRepository.save(user), UserDto.class);

        return new RegisterResponse(dto, HttpStatus.CREATED);
    }

    public LoginResponse login(LoginForm loginForm) {
        Optional<UserEntity> user = this.userRepository.findUserByEmail(loginForm.getEmail());

        if (user.isPresent() && passwordEncoder.matches(loginForm.getPassword(), user.get().getPassword())) {
            return new LoginResponse(modelMapper.map(user.get(), UserDto.class), HttpStatus.OK);
        }

        return new LoginResponse(null, HttpStatus.UNAUTHORIZED);
    }
}
