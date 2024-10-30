package org.example.cookbook.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    @Size(min = 3, max = 30)
    private String firstName;

    @Size(min = 3, max = 30)
    private String lastName;

    @Email
    private String email;

    @Size(min = 3, max = 30)
    private String password;
}
