package org.example.cookbook.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String role;
}
