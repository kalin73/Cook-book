package org.example.cookbook.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cookbook.model.dto.user.LoginForm;
import org.example.cookbook.model.dto.user.RegisterForm;
import org.example.cookbook.repository.LoginLogRepository;
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

    @Autowired
    private LoginLogRepository loginLogRepository;

    @AfterEach
    public void tearDown() {
        loginLogRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void registerUserTest() throws Exception {
        final String json = mapToJson(registerForm);

        assertEquals(0, userRepository.count());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)))
                .andExpect(jsonPath("$.email", is(email)));

        assertEquals(1, userRepository.count());
    }

    @Test
    public void failedRegistrationTest() throws Exception {
        addUser();

        String json = new ObjectMapper().writeValueAsString(registerForm);

        assertEquals(1, userRepository.count());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        assertEquals(1, userRepository.count());
    }

    @Test
    public void testRegisterUserWithWrongInput() throws Exception {
        RegisterForm wrongUser = new RegisterForm("er", lastName, email, password);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(wrongUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].fieldName", is("firstName")));

        // Fixing field
        wrongUser.setFirstName(firstName);

        wrongUser.setLastName("er");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(wrongUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].fieldName", is("lastName")));

        // Fixing field
        wrongUser.setLastName(lastName);

        wrongUser.setPassword("12");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(wrongUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0].fieldName", is("password")));

        assertEquals(0, userRepository.count());
    }

    @Test
    public void successfulLoginTest() throws Exception {
        addUser();

        final String requestJson = mapToJson(loginForm);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", is(email)));
    }

    @Test
    public void failedLoginTest() throws Exception {
        addUser();

        String json = mapToJson(new LoginForm(email, password + "wrong"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());

        json = mapToJson(new LoginForm("wrong@emal.com", password));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginWithInvalidInputTest() throws Exception {
        addUser();

        final String json = mapToJson(new LoginForm("null", password));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    private void addUser() {
        userService.registerUser(registerForm);
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
