package org.example.cookbook.web.rest;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.model.dto.recipe.RecipeCreateForm;
import org.example.cookbook.model.dto.recipe.RecipeDto;
import org.example.cookbook.model.dto.user.ErrorResponse;
import org.example.cookbook.model.user.CustomUserDetails;
import org.example.cookbook.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<Object> createRecipe(@RequestBody @Validated RecipeCreateForm recipeCreateForm,
                                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<ErrorResponse> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> new ErrorResponse(e.getField(), e.getDefaultMessage()))
                    .toList();

            return ResponseEntity.badRequest().body(errors);
        }

        RecipeDto recipe = this.recipeService.createRecipe(recipeCreateForm);

        this.recipeService.refreshRecipes();

        return ResponseEntity.created(ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(recipe.getId())
                        .toUri())
                .body(recipe);
    }

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
        return new ResponseEntity<>(this.recipeService.getAllRecipes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable(name = "id") UUID id) {
        RecipeDto recipe = this.recipeService.getRecipeById(id);

        if (recipe == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || @recipeService.isOwner(#owner, #id)")
    public ResponseEntity<RecipeDto> updateRecipe(@RequestBody RecipeCreateForm updatedRecipe,
                                                  @PathVariable(name = "id") UUID id,
                                                  @AuthenticationPrincipal CustomUserDetails owner) {
        RecipeDto recipe = this.recipeService.updateRecipe(updatedRecipe, id);

        this.recipeService.refreshRecipes();

        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || @recipeService.isOwner(#owner, #id)")
    public ResponseEntity<RecipeDto> deleteRecipeById(@PathVariable(name = "id") UUID id, @AuthenticationPrincipal CustomUserDetails owner) {
        this.recipeService.deleteRecipeById(id);

        this.recipeService.refreshRecipes();

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDto>> searchRecipe(@RequestParam(name = "title") String title) {
        List<RecipeDto> recipes = this.recipeService.searchRecipes(title);
        HttpStatus status = recipes.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;

        return new ResponseEntity<>(recipes, status);
    }
}
