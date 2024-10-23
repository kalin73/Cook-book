package org.example.cookbook.web.rest;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.user.*;
import org.example.cookbook.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterForm registerForm) {
        RegisterResponse response = this.userService.registerUser(registerForm);

        return new ResponseEntity<>(response.user(), response.status());
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Validated LoginForm loginForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        LoginResponse response = this.userService.login(loginForm);

        return new ResponseEntity<>(response.user(), response.status());
    }
}
