package org.example.cookbook.service;

import org.example.cookbook.model.dto.user.LoginForm;
import org.example.cookbook.model.dto.user.LoginResponse;
import org.example.cookbook.model.dto.user.RegisterForm;
import org.example.cookbook.model.dto.user.UserDto;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestUserService {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @Captor
    private ArgumentCaptor<UserEntity> captor;

    @BeforeEach
    public void setUp() {
        this.userService = new UserService(userRepository, passwordEncoder, modelMapper);
    }

    @Test
    public void registerUserTest() {
        final String password = "password";
        final String encodedPassword = "encoded_password";

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        RegisterForm userToRegister = new RegisterForm("Ivan", "Ivanov", "ivan@abv.bg", password);

        userService.registerUser(userToRegister);

        verify(userRepository).save(captor.capture());

        UserEntity user = captor.getValue();

        assertEquals(encodedPassword, user.getPassword());
        assertEquals("Ivan", user.getFirstName());
        assertEquals("Ivanov", user.getLastName());
        assertEquals("ivan@abv.bg", user.getEmail());

    }

    @Test
    public void loginTest() {
        final String firstName = "Ivan";
        final String lastName = "Ivanov";
        final String email = "ivan@abv.bg";
        final String password = "pass";
        final String id = UUID.randomUUID().toString();

        LoginForm loginForm = new LoginForm(email, password);
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(new UserEntity(firstName, lastName,
                email,
                password,
                null,
                null)));
        when(passwordEncoder.matches(password, password)).thenReturn(true);
        when(modelMapper.map(new UserEntity(), UserDto.class)).thenReturn(new UserDto(id, firstName, lastName, email));

        LoginResponse loginResponse = this.userService.login(loginForm);

        assertEquals(loginResponse.status(), HttpStatus.OK);
        assertEquals(email, loginResponse.user().getEmail());
    }
}
