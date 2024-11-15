package org.example.cookbook.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cookbook.model.dto.user.LoginForm;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.model.enums.Role;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestLogController {
    private static final String TEST_EMAIL = "test@test.com";

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
        user.setEmail(TEST_EMAIL);
        user.setFirstName("Test");
        user.setLastName("Testov");
        user.setPassword(passwordEncoder.encode("test123"));
        user.setRole(Role.ADMIN);

        userRepository.save(user);
    }

    @AfterEach
    public void teardown() {
        loginLogRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetLoginLogByUserEmail() throws Exception {
        login(TEST_EMAIL);

        mockMvc.perform(get("/api/log/login/{email}", TEST_EMAIL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].user.email").value(TEST_EMAIL));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetLoginLogWrongEmail() throws Exception {
        login(TEST_EMAIL);

        mockMvc.perform(get("/api/log/login/{email}", "test@test1.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetLoginLogNotAsAdmin() throws Exception {
        login(TEST_EMAIL);

        mockMvc.perform(get("/api/log/login/{email}", TEST_EMAIL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllLoginLogs() throws Exception {
        login(TEST_EMAIL);
        login("test2@test.com");

        mockMvc.perform(get("/api/log/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllLogsWhenNoLogsAvailable() throws Exception {
        mockMvc.perform(get("/api/log/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private void login(String email) throws Exception {
        LoginForm loginForm = new LoginForm(email, "test123");
        String json = new ObjectMapper().writeValueAsString(loginForm);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }
}
