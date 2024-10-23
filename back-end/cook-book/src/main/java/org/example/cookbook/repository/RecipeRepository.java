package org.example.cookbook.repository;

import org.example.cookbook.model.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<RecipeEntity, UUID> {
    @Query("SELECT r FROM RecipeEntity r WHERE LOWER(r.title) LIKE %:title%")
    Optional<List<RecipeEntity>> searchByTitle(String title);
}
