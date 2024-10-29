package org.example.cookbook.service;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.recipe.RecipeCreateForm;
import org.example.cookbook.model.dto.recipe.RecipeDto;
import org.example.cookbook.model.entity.IngredientEntity;
import org.example.cookbook.model.entity.RecipeEntity;
import org.example.cookbook.model.entity.UserEntity;
import org.example.cookbook.model.user.CustomUserDetails;
import org.example.cookbook.repository.IngredientRepository;
import org.example.cookbook.repository.RecipeRepository;
import org.example.cookbook.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final IngredientRepository ingredientRepository;

    @Cacheable(cacheNames = "recipes")
    public List<RecipeDto> getAllRecipes() {
        return this.recipeRepository.findAll()
                .stream()
                .map(r -> modelMapper.map(r, RecipeDto.class))
                .toList();
    }

    public RecipeDto createRecipe(RecipeCreateForm recipeCreateForm) {
        UserEntity user = this.userRepository.findById(UUID.fromString(recipeCreateForm.getUserId()))
                .orElseThrow(IllegalArgumentException::new);

        RecipeEntity recipe = new RecipeEntity()
                .setTitle(recipeCreateForm.getTitle())
                .setPreparation(recipeCreateForm.getPreparation())
                .setImageUrl(recipeCreateForm.getImageUrl())
                .setUser(user);

        recipe = this.recipeRepository.save(recipe);

        List<IngredientEntity> ingredients = extractIngredients(recipeCreateForm, recipe);

        ingredients = this.ingredientRepository.saveAll(ingredients);

        recipe.setIngredients(ingredients);

        return modelMapper.map(recipe, RecipeDto.class);
    }

    public RecipeDto getRecipeById(UUID id) {
        return this.recipeRepository.findById(id)
                .map(r -> modelMapper.map(r, RecipeDto.class))
                .orElse(null);
    }

    @Transactional
    public RecipeDto updateRecipe(RecipeCreateForm updatedRecipe, UUID id) {
        RecipeEntity recipe = this.recipeRepository.findById(id).orElse(null);
        recipe.setTitle(updatedRecipe.getTitle())
                .setPreparation(updatedRecipe.getPreparation())
                .setImageUrl(updatedRecipe.getImageUrl())
                .setIngredients(null);

        recipe = this.recipeRepository.save(recipe);

        this.ingredientRepository.deleteAllByRecipeId(recipe);

        List<IngredientEntity> ingredients = extractIngredients(updatedRecipe, recipe);

        ingredients = this.ingredientRepository.saveAll(ingredients);

        recipe.setIngredients(ingredients);

        return modelMapper.map(recipe, RecipeDto.class);
    }

    public void deleteRecipeById(UUID id) {
        this.recipeRepository.deleteById(id);
    }

    private List<IngredientEntity> extractIngredients(RecipeCreateForm recipeCreateForm, RecipeEntity recipe) {
        String[] arr = recipeCreateForm.getIngredients().split("\\r?\\n");
        List<IngredientEntity> ingredients = new ArrayList<>();

        for (String ingredient : arr) {
            String name = ingredient.split("-")[0];
            String quantity = ingredient.split("-")[1];
            ingredients.add(new IngredientEntity(name, quantity, recipe));
        }

        return ingredients;
    }

    public List<RecipeDto> searchRecipes(String title) {
        List<RecipeEntity> recipeEntities = this.recipeRepository.searchByTitle(title.toLowerCase())
                .orElse(null);

        if (recipeEntities == null) {
            return new ArrayList<>();
        }

        return recipeEntities.stream()
                .map(recipe -> modelMapper.map(recipe, RecipeDto.class))
                .toList();
    }

    public boolean isOwner(CustomUserDetails owner, UUID recipeId) {
        RecipeEntity recipe = this.recipeRepository.findById(recipeId).orElseThrow();

        return recipe.getUser().getEmail().equals(owner.getUsername());
    }

    @CacheEvict(cacheNames = "recipes", allEntries = true)
    public void refreshRecipes() {

    }
}
