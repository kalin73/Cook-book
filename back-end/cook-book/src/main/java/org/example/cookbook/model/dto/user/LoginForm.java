package org.example.cookbook.model.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @Size(min = 5, max = 30)
    private String email;

    @Size(min = 3)
    private String password;
}
