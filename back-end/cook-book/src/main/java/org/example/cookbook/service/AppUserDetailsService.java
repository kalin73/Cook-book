package org.example.cookbook.service;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.model.user.CustomUserDetails;
import org.example.cookbook.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = this.userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " was not found!"));

        return new CustomUserDetails(user);
    }
}
