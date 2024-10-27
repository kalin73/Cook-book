package org.example.cookbook.model.user;

import lombok.Getter;
import lombok.Setter;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.model.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails extends User {
    private String firstName;
    private String lastName;
    private Role role;

    public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
    }

    public CustomUserDetails(UserEntity user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();
    }
}
