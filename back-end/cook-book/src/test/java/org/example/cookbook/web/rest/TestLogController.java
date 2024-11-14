package org.example.cookbook.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cookbook.model.dto.user.LoginForm;
import org.example.cookbook.model.entity.LoginLogEntity;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.repository.LoginLogRepository;
import org.example.cookbook.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestLogController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        UserEntity user = new UserEntity();
        user.setEmail("test@test.com");
        user.setFirstName("Test");
        user.setLastName("Testov");
        user.setPassword(passwordEncoder.encode("test123"));
        user = userRepository.save(user);

        LoginLogEntity log = new LoginLogEntity();
        log.setUser(user);
        log.setDate(LocalDateTime.now());
        loginLogRepository.save(log);
    }

    @AfterEach
    public void teardown() {
        loginLogRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetLoginLogByUserEmail() throws Exception {
        LoginForm loginForm = new LoginForm("test@test.com", "test123");
        String json = new ObjectMapper().writeValueAsString(loginForm);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/api/log/login/{email}", "test@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].user.email").value("test@test.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetLoginLogWrongEmail() throws Exception {
        LoginForm loginForm = new LoginForm("test@test.com", "test123");
        String json = new ObjectMapper().writeValueAsString(loginForm);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        mockMvc.perform(get("/api/log/login/{email}", "test@test1.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
