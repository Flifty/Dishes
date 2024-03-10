package com.example.dishes.repository;

import com.example.dishes.entity.IngredientEntity;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepos extends CrudRepository<IngredientEntity, Long> {
    IngredientEntity findByName(String name);
}
