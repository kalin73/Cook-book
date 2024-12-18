package org.example.cookbook.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "[A-Za-z]{3,30}", message = "First name should be between 3 and 30 letters!")
    private String firstName;

    @Pattern(regexp = "[A-Za-z]{3,30}", message = "Last name should be between 3 and 30 letters!")
    private String lastName;

    @Email
    private String email;

    @Size(min = 3, max = 30, message = "Password should be between 3 and 30 symbols!")
    private String password;
}
