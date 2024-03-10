package com.example.dishes.service;

import com.example.dishes.entity.DishEntity;
import com.example.dishes.entity.IngredientEntity;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.exception.IngredientNotFoundException;
import com.example.dishes.exception.IngredientAlreadyExistException;
import com.example.dishes.model.Ingredient;
import com.example.dishes.repository.DishRepos;
import com.example.dishes.repository.IngredientRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class IngredientService {

    private final IngredientRepos ingredientRepos;
    private final DishRepos dishRepos;

    @Autowired
    public IngredientService(IngredientRepos ingredientRepos, DishRepos dishRepos) {
        this.ingredientRepos = ingredientRepos;
        this.dishRepos = dishRepos;
    }

    @Transactional
    public void addIngredient(Long dishId, IngredientEntity ingredient) throws DishNotFoundException, IngredientAlreadyExistException {
        DishEntity dishEntity = dishRepos.findById(dishId).orElse(null);
        if (dishEntity == null)
            throw new DishNotFoundException("Не удалось добавить ингредиент. Блюдо не найдено");

        if (dishEntity.getIngredientList().stream().anyMatch(existingIngredient -> existingIngredient.getName().equals(ingredient.getName()))) {
            throw new IngredientAlreadyExistException("Ингредиент уже есть в данном блюде");
        }

        IngredientEntity existingIngredient = ingredientRepos.findByName(ingredient.getName());

        if (existingIngredient != null) {
            dishEntity.getIngredientList().add(existingIngredient);
        } else {
            ingredientRepos.save(ingredient);
            dishEntity.getIngredientList().add(ingredient);
        }

        dishRepos.save(dishEntity);
    }

    public Ingredient getIngredient(Long id) throws IngredientNotFoundException {
        IngredientEntity ingredient = ingredientRepos.findById(id).orElse(null);
        if (ingredient != null) {
            return Ingredient.toModel(ingredient);
        } else {
            throw new IngredientNotFoundException("Ингредиент не найден");
        }
    }

    public void updateIngredient(Long id, IngredientEntity ingredient) throws IngredientNotFoundException {
        IngredientEntity ingredientEntity = ingredientRepos.findById(id).orElse(null);
        if (ingredientEntity != null) {
            ingredientEntity.setName(ingredient.getName());
            ingredientRepos.save(ingredientEntity);
        } else {
            throw new IngredientNotFoundException("Ингредиент не найден");
        }
    }

    @Transactional
    public void deleteIngredient(Long dishId, Long ingredientId) throws IngredientNotFoundException, DishNotFoundException {
        DishEntity dishEntity = dishRepos.findById(dishId).orElse(null);
        if (dishEntity == null)
            throw new DishNotFoundException("Блюдо не найдено");

        IngredientEntity ingredientEntity = ingredientRepos.findById(ingredientId).orElse(null);
        if (ingredientEntity == null)
            throw new IngredientNotFoundException("Ингредиент не найден");

        dishEntity.getIngredientList().remove(ingredientEntity);
        dishRepos.save(dishEntity);
        ingredientEntity.getDishList().remove(dishEntity);
        ingredientRepos.save(ingredientEntity);
    }
}
