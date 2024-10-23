package org.example.cookbook.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cookbook.model.dto.user.*;
import org.example.cookbook.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestAuthController {
    final String firstName = "Ivan";
    final String lastName = "Ivanov";
    final String email = "ivan@abv.bg";
    final String password = "123";
    final String id = UUID.randomUUID().toString();

    final LoginForm loginForm = new LoginForm(email, password);

    final UserDto user = new UserDto(id, firstName, lastName, email);

    final RegisterForm registerForm = new RegisterForm(firstName, lastName, email, password);

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void successfulLoginTest() throws Exception {
        final String requestJson = new ObjectMapper().writeValueAsString(loginForm);

        when(userService.login(any())).thenReturn(new LoginResponse(user, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.email", is(email)));
    }

    @Test
    public void failedLoginTest() throws Exception {
        when(userService.login(any())).thenReturn(new LoginResponse(null, HttpStatus.UNAUTHORIZED));

        final String json = new ObjectMapper().writeValueAsString(loginForm);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void registerUserTest() throws Exception {
        final RegisterResponse regResponse = new RegisterResponse(user, HttpStatus.CREATED);

        when(userService.registerUser(any())).thenReturn(regResponse);

        final String json = new ObjectMapper().writeValueAsString(registerForm);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)))
                .andExpect(jsonPath("$.email", is(email)));
    }

    @Test
    public void failedRegistrationTest() throws Exception {
        when(userService.registerUser(any())).thenReturn(new RegisterResponse(null, HttpStatus.CONFLICT));

        String json = new ObjectMapper().writeValueAsString(registerForm);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());
    }
}
