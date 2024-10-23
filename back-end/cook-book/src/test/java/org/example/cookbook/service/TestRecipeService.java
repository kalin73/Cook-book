package org.example.cookbook.service;

import org.example.cookbook.model.dto.recipe.RecipeDto;
import org.example.cookbook.model.entity.RecipeEntity;
import org.example.cookbook.repository.IngredientRepository;
import org.example.cookbook.repository.RecipeRepository;
import org.example.cookbook.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestRecipeService {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Captor
    private ArgumentCaptor<RecipeEntity> captor;

    private ModelMapper modelMapper;

    private RecipeService recipeService;

    @BeforeEach
    public void setUp() {
        this.modelMapper = new ModelMapper();
        this.recipeService = new RecipeService(recipeRepository, userRepository, modelMapper, ingredientRepository);
    }

    @Test
    public void getAllRecipesTest() {
        when(recipeRepository.findAll()).thenReturn(List.of(new RecipeEntity("Pizza", null, null, null, null),
                new RecipeEntity("Soup", null, null, null, null)));
        List<RecipeDto> recipes = this.recipeService.getAllRecipes();

        assertEquals("Pizza", recipes.get(0).getTitle());
        assertEquals("Soup", recipes.get(1).getTitle());
    }

}
