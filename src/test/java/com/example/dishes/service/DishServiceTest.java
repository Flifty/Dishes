package com.example.dishes.service;

import com.example.dishes.component.Cache;
import com.example.dishes.dto.DishDTO;
import com.example.dishes.entity.Dish;
import com.example.dishes.exception.DishAlreadyExistException;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DishServiceTest {

  @Mock
  private DishRepository dishRepository;

  @Mock
  private Cache<String, DishDTO> dishCache;

  @InjectMocks
  private DishService dishService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testAddDish() {
    Dish dish = new Dish();
    dish.setName("Test Dish");
    dish.setCountry("Test Country");
    dish.setCategory("Test Category");
    dish.setInstruction("Test Instruction");

    when(dishRepository.findByName("Test Dish")).thenReturn(null);

    assertDoesNotThrow(() -> dishService.addDish(dish));

    when(dishRepository.findByName("Test Dish")).thenReturn(dish);

    assertThrows(DishAlreadyExistException.class, () -> dishService.addDish(dish));
  }

  @Test
  public void testGetDish() {
    String dishName = "Test Dish";
    Dish dish = new Dish();
    dish.setName(dishName);

    DishDTO dishDTO = new DishDTO();
    dishDTO.setName(dishName);

    when(dishCache.containsKey(dishName)).thenReturn(true);
    when(dishCache.get(dishName)).thenReturn(dishDTO);

    assertDoesNotThrow(() -> dishService.getDish(dishName));

    when(dishCache.containsKey(dishName)).thenReturn(false);
    when(dishRepository.findByName(dishName)).thenReturn(dish);

    assertDoesNotThrow(() -> {
      try {
        dishService.getDish(dishName);
      } catch (DishNotFoundException e) {
        fail("DishNotFoundException should not be thrown when dish exists");
      }
    });

    when(dishCache.containsKey(dishName)).thenReturn(false);
    when(dishRepository.findByName(dishName)).thenReturn(null);

    assertThrows(DishNotFoundException.class, () -> dishService.getDish(dishName));
  }


  @Test
  public void testGetDishesWithIngredient() {
    Long ingredientId = 123L;
    String cacheKey = "ingredient_" + ingredientId;

    when(dishCache.containsKey(cacheKey)).thenReturn(true);

    assertDoesNotThrow(() -> dishService.getDishesWithIngredient(ingredientId));

    when(dishCache.containsKey(cacheKey)).thenReturn(false);
    when(dishRepository.findDishesByIngredientList_Id(ingredientId)).thenReturn(new ArrayList<>());

    assertThrows(DishNotFoundException.class, () -> dishService.getDishesWithIngredient(ingredientId));
  }

  @Test
  public void testGetByName() throws DishNotFoundException {
    String name = "Test Name";

    when(dishCache.containsKey(name)).thenReturn(true);

    assertDoesNotThrow(() -> dishService.getByName(name));

    when(dishCache.containsKey(name)).thenReturn(false);
    when(dishRepository.findByName(name)).thenReturn(null);

    assertThrows(DishNotFoundException.class, () -> dishService.getByName(name));
  }

  @Test
  public void testUpdateDish() throws DishNotFoundException {
    String name = "Test Name";
    Dish dish = new Dish();
    dish.setName(name);

    when(dishRepository.findByName(name)).thenReturn(dish);

    assertDoesNotThrow(() -> dishService.updateDish(name, dish));

    when(dishRepository.findByName(name)).thenReturn(null);

    assertThrows(DishNotFoundException.class, () -> dishService.updateDish(name, dish));
  }

  @Test
  public void testDeleteDish() throws DishNotFoundException {
    Long id = 123L;
    Dish dish = new Dish();
    dish.setId(id);

    when(dishRepository.findById(id)).thenReturn(java.util.Optional.of(dish));

    assertDoesNotThrow(() -> dishService.deleteDish(id));

    when(dishRepository.findById(id)).thenReturn(java.util.Optional.empty());

    assertThrows(DishNotFoundException.class, () -> dishService.deleteDish(id));
  }
}



