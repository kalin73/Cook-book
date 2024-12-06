package org.example.cookbook.model.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCreateForm {
    @Size(min = 1, max = 50, message = "Title should not be empty or more then 50 symbols")
    private String title;

    @NotBlank(message = "Preparation should not be empty")
    private String preparation;

    @NotBlank(message = "Image url should not be empty")
    private String imageUrl;

    @NotBlank(message = "Ingredients should not be empty")
    private String ingredients;

    private String userId;
}
