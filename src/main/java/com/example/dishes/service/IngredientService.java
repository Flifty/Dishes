package com.example.dishes.service;

import com.example.dishes.dto.IngredientDTO;
import com.example.dishes.entity.Dish;
import com.example.dishes.entity.Ingredient;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.exception.IngredientAlreadyExistException;
import com.example.dishes.exception.IngredientNotFoundException;
import com.example.dishes.repository.DishRepository;
import com.example.dishes.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IngredientService {

  private final IngredientRepository ingredientRepository;
  private final DishRepository dishRepository;
  private static final String INGREDIENT_NOT_FOUND_STRING = "Ингредиент не найден";

  @Autowired
  public IngredientService(IngredientRepository ingredientRepository,
                           DishRepository dishRepository) {
    this.ingredientRepository = ingredientRepository;
    this.dishRepository = dishRepository;
  }

  @Transactional
  public void addIngredient(Long dishId, Ingredient ingredient)
      throws DishNotFoundException, IngredientAlreadyExistException {
    Dish dish = dishRepository.findById(dishId).orElse(null);
      if (dish == null) {
          throw new DishNotFoundException("Не удалось добавить ингредиент. Блюдо не найдено");
      }

    if (dish.getIngredientList().stream().anyMatch(
        existingIngredient -> existingIngredient.getName().equals(ingredient.getName()))) {
      throw new IngredientAlreadyExistException("Ингредиент уже есть в данном блюде");
    }

    Ingredient existingIngredient = ingredientRepository.findByName(ingredient.getName());

    if (existingIngredient != null) {
      dish.getIngredientList().add(existingIngredient);
    } else {
      ingredientRepository.save(ingredient);
      dish.getIngredientList().add(ingredient);
    }

    dishRepository.save(dish);
  }

  public IngredientDTO getIngredient(Long id) throws IngredientNotFoundException {
    Ingredient ingredient = ingredientRepository.findById(id).orElse(null);
    if (ingredient != null) {
      return IngredientDTO.toModel(ingredient);
    } else {
      throw new IngredientNotFoundException(INGREDIENT_NOT_FOUND_STRING);
    }
  }

  public void updateIngredient(Long id, Ingredient ingredient) throws IngredientNotFoundException {
    Ingredient ingredientEntity = ingredientRepository.findById(id).orElse(null);
    if (ingredientEntity != null) {
      ingredientEntity.setName(ingredient.getName());
      ingredientRepository.save(ingredientEntity);
    } else {
      throw new IngredientNotFoundException(INGREDIENT_NOT_FOUND_STRING);
    }
  }

  @Transactional
  public void deleteIngredient(Long dishId, Long ingredientId)
      throws IngredientNotFoundException, DishNotFoundException {
    Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
    if (ingredient == null) {
      throw new IngredientNotFoundException(INGREDIENT_NOT_FOUND_STRING);
    }

    Dish dish = dishRepository.findById(dishId).orElse(null);
    if (dish == null) {
      throw new DishNotFoundException("Блюдо не найдено");
    }

    dish.getIngredientList().remove(ingredient);
    dishRepository.save(dish);
    ingredient.getDishList().remove(dish);
    ingredientRepository.save(ingredient);
  }

}
