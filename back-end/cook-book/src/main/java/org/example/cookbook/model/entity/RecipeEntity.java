package org.example.cookbook.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "recipes")
@DynamicUpdate
public class RecipeEntity extends BaseEntity {
    @Column
    private String title;

    @Column
    private String preparation;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipeId", cascade = CascadeType.ALL)
    private List<IngredientEntity> ingredients;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public RecipeEntity() {

    }

    public RecipeEntity(String title, String preparation, String imageUrl, List<IngredientEntity> ingredients, UserEntity user) {
        this.title = title;
        this.preparation = preparation;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public RecipeEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPreparation() {
        return preparation;
    }

    public RecipeEntity setPreparation(String preparation) {
        this.preparation = preparation;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public RecipeEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public List<IngredientEntity> getIngredients() {
        return ingredients;
    }

    public RecipeEntity setIngredients(List<IngredientEntity> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public RecipeEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
