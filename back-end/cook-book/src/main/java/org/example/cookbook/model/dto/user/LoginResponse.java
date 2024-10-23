package org.example.cookbook.model.dto.user;

import org.springframework.http.HttpStatus;

public record LoginResponse(UserDto user, HttpStatus status) {
}
