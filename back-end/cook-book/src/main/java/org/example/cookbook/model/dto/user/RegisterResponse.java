package org.example.cookbook.model.dto.user;

import org.springframework.http.HttpStatus;

public record RegisterResponse(UserDto user, HttpStatus status) {
}
