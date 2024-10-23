package org.example.cookbook.service;

import lombok.RequiredArgsConstructor;
import org.example.cookbook.repository.IngredientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    
}
