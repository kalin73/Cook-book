package org.example.cookbook.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cookbook.model.dto.user.LoginForm;
import org.example.cookbook.model.dto.user.RegisterForm;
import org.example.cookbook.repository.UserRepository;
import org.example.cookbook.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestAuthController {
    final String firstName = "Ivan";
    final String lastName = "Ivanov";
    final String email = "ivan@abv.bg";
    final String password = "123";

    final LoginForm loginForm = new LoginForm(email, password);

    final RegisterForm registerForm = new RegisterForm(firstName, lastName, email, password);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void successfulLoginTest() throws Exception {
        addUser();

        final String requestJson = new ObjectMapper().writeValueAsString(loginForm);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)));
    }

    @Test
    public void failedLoginTest() throws Exception {
        addUser();

        String json = new ObjectMapper().writeValueAsString(new LoginForm(email, password + "wrong"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());

        json = new ObjectMapper().writeValueAsString(new LoginForm("wrong@emal.com", password));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginWithInvalidInputTest() throws Exception {
        addUser();

        final String json = new ObjectMapper()
                .writeValueAsString(new LoginForm("null", password));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUserTest() throws Exception {
        final String json = new ObjectMapper().writeValueAsString(registerForm);

        assertEquals(userRepository.count(), 0);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)))
                .andExpect(jsonPath("$.email", is(email)));

        assertEquals(userRepository.count(), 1);
    }

    @Test
    public void failedRegistrationTest() throws Exception {
        addUser();

        String json = new ObjectMapper().writeValueAsString(registerForm);

        assertEquals(userRepository.count(), 1);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        assertEquals(userRepository.count(), 1);
    }

    private void addUser() {
        userService.registerUser(registerForm);
    }
}
