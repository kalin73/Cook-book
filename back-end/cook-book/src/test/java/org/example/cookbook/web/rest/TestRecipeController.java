package org.example.cookbook.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cookbook.model.dto.recipe.RecipeCreateForm;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.repository.RecipeRepository;
import org.example.cookbook.repository.UserRepository;
import org.example.cookbook.service.RecipeService;
import org.hibernate.validator.internal.constraintvalidators.hv.UUIDValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class TestRecipeController {
    private static String USER_ID = null;
    private static String RECIPE_ID_1 = null;
    private static String RECIPE_ID_2 = null;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UUIDValidator uUIDValidator;

    @BeforeEach
    public void setUp() {
        UserEntity user = new UserEntity();
        user.setCreatedAt(LocalDateTime.now())
                .setEmail("user1@example.com")
                .setPassword(passwordEncoder.encode("12345"));
        user = userRepository.save(user);
        USER_ID = user.getId().toString();

        RECIPE_ID_1 = recipeService
                .createRecipe(new RecipeCreateForm("Pizza", "cook", "url1", "dough-500gr", USER_ID))
                .getId();
    }

    @AfterEach
    public void tearDown() {
        recipeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGettingAllRecipes() throws Exception {
        mockMvc.perform(get("/api/recipe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title", is("Pizza")))
                .andExpect(jsonPath("$.[0].imageUrl", is("url1")))
                .andExpect(jsonPath("$.[0].ingredients[0].name", is("dough")))
                .andExpect(jsonPath("$.[0].ingredients[0].quantity", is("500gr")))
                .andExpect(jsonPath("$.[0].userId", is(USER_ID)));

    }

    @Test
    public void getRecipeByIdTest() throws Exception {
        createNewRecipe();

        mockMvc.perform(get("/api/recipe/{id}", RECIPE_ID_1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Pizza"))
                .andExpect(jsonPath("$.ingredients[0].name", is("dough")))
                .andExpect(jsonPath("$.ingredients[0].quantity", is("500gr")))
                .andExpect(jsonPath("$.userId", is(USER_ID)));

        mockMvc.perform(get("/api/recipe/{id}", RECIPE_ID_2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Lasagna"))
                .andExpect(jsonPath("$.ingredients[0].name", is("cheese")))
                .andExpect(jsonPath("$.ingredients[0].quantity", is("500gr")))
                .andExpect(jsonPath("$.userId", is(USER_ID)));
    }

    @Test
    public void getRecipeWithWrongId() throws Exception {
        mockMvc.perform(get("/api/recipe/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateRecipe() throws Exception {
        RecipeCreateForm recipeCreateForm = new RecipeCreateForm();
        recipeCreateForm.setTitle("Pizza");
        recipeCreateForm.setImageUrl("url1");
        recipeCreateForm.setPreparation("cook");
        recipeCreateForm.setIngredients("dough-500gr");
        recipeCreateForm.setUserId(USER_ID);

        String json = new ObjectMapper().writeValueAsString(recipeCreateForm);

        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title", is("Pizza")))
                .andExpect(jsonPath("$.ingredients[0].name", is("dough")))
                .andExpect(jsonPath("$.ingredients[0].quantity", is("500gr")));
    }

    @Test
    public void testUpdateRecipe() throws Exception {
        mockMvc.perform(get("/api/recipe/{id}", RECIPE_ID_1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Pizza")))
                .andExpect(jsonPath("$.ingredients[0].name", is("dough")));

        RecipeCreateForm recipeCreateForm = new RecipeCreateForm();
        recipeCreateForm.setTitle("French fries");
        recipeCreateForm.setImageUrl("url1");
        recipeCreateForm.setPreparation("fry");
        recipeCreateForm.setIngredients("potatoes-5");
        String json = new ObjectMapper().writeValueAsString(recipeCreateForm);

        mockMvc.perform(patch("/api/recipe/{id}", RECIPE_ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("French fries")))
                .andExpect(jsonPath("$.ingredients[0].name", is("potatoes")));
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        createNewRecipe();

        assertEquals(recipeRepository.count(), 2);

        mockMvc.perform(delete("/api/recipe/{id}", RECIPE_ID_2))
                .andExpect(status().isNoContent());

        assertEquals(recipeRepository.count(), 1);

        mockMvc.perform(get("/api/recipe/{id}", RECIPE_ID_2))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchRecipe() throws Exception {
        createNewRecipe();
        mockMvc.perform(get("/api/recipe/search")
                        .param("title", "Pizza")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title", is("Pizza")))
                .andExpect(jsonPath("$.[0].imageUrl", is("url1")))
                .andExpect(jsonPath("$.[0].ingredients[0].name", is("dough")));
    }

    @Test
    public void testSearchNonExistentRecipe() throws Exception {
        mockMvc.perform(get("/api/recipe/search")
                        .param("title", "Lasagna"))
                .andExpect(status().isNotFound());

    }

    private void createNewRecipe() {
        RECIPE_ID_2 = recipeService
                .createRecipe(new RecipeCreateForm("Lasagna", "cook", "url1", "cheese-500gr", USER_ID))
                .getId();
    }
}
