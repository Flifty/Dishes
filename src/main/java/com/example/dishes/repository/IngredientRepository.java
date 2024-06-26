package com.example.dishes.repository;

import com.example.dishes.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
  Ingredient findByName(String name);
}
