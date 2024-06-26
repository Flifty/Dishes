package com.example.dishes.service;

import com.example.dishes.component.Cache;
import com.example.dishes.dto.DishDTO;
import com.example.dishes.entity.Dish;
import com.example.dishes.exception.DishAlreadyExistException;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.repository.DishRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DishServiceTest {

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
  void testAddDish() {
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
  void testAddDishesBulk() {
    // Prepare dishes
    List<Dish> dishes = new ArrayList<>();
    Dish existingDish = new Dish();
    existingDish.setName("Existing Dish");
    Dish newDish = new Dish();
    newDish.setName("New Dish");
    dishes.add(existingDish);
    dishes.add(newDish);

    // Mock repository behavior
    when(dishRepository.findByName("Existing Dish")).thenReturn(existingDish);
    when(dishRepository.findByName("New Dish")).thenReturn(null);

    // Call the method and assert the result
    List<String> errors = dishService.addDishesBulk(dishes);
    assertEquals(1, errors.size());
    assertEquals("Блюдо 'Existing Dish' уже существует", errors.get(0));

    // Verify repository save method is called only once
    verify(dishRepository, times(1)).save(newDish);
  }


  @Test
  void testGetDish() {
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
  void testGetDish_WhenCacheIsEmptyAndDishFoundInRepository() throws DishNotFoundException {
    String name = "Test Dish";
    Dish dish = new Dish();
    dish.setName(name);
    dish.setCountry("Test Country");
    dish.setCategory("Test Category");
    dish.setInstruction("Test Instruction");

    when(dishCache.containsKey(name)).thenReturn(false);
    when(dishRepository.findByName(name)).thenReturn(dish);

    DishDTO result = dishService.getDish(name);

    assertNotNull(result);
    assertEquals(name, result.getName());
    assertEquals("Test Country", result.getCountry());
    assertEquals("Test Category", result.getCategory());
    assertEquals("Test Instruction", result.getInstruction());

    verify(dishCache, times(1)).put(name, result);
  }

  @Test
  void testGetDishesWithIngredient() {
    Long ingredientId = 123L;
    String cacheKey = "ingredient_" + ingredientId;

    when(dishCache.containsKey(cacheKey)).thenReturn(true);

    assertDoesNotThrow(() -> dishService.getDishesWithIngredient(ingredientId));

    when(dishCache.containsKey(cacheKey)).thenReturn(false);
    when(dishRepository.findDishesByIngredientList_Id(ingredientId)).thenReturn(new ArrayList<>());

    assertThrows(DishNotFoundException.class, () -> dishService.getDishesWithIngredient(ingredientId));
  }

  @Test
  void testGetDishesWithIngredient_WhenDishesExist() throws DishNotFoundException {
    Long ingredientId = 1L;
    String cacheKey = "ingredient_" + ingredientId;
    List<Dish> dishes = new ArrayList<>();
    Dish dish1 = new Dish();
    dish1.setName("Dish 1");
    Dish dish2 = new Dish();
    dish2.setName("Dish 2");
    dishes.add(dish1);
    dishes.add(dish2);

    when(dishCache.containsKey(cacheKey)).thenReturn(false);
    when(dishRepository.findDishesByIngredientList_Id(ingredientId)).thenReturn(dishes);

    List<DishDTO> dishDTOs = dishService.getDishesWithIngredient(ingredientId);

    assertEquals(2, dishDTOs.size());
    assertEquals("Dish 1", dishDTOs.get(0).getName());
    assertEquals("Dish 2", dishDTOs.get(1).getName());

    verify(dishCache, times(1)).putList(cacheKey, dishDTOs);
  }

  @Test
  void testGetDishesWithIngredient_WhenCacheIsEmptyAndDishesFoundInRepository() throws DishNotFoundException {
    Long ingredientId = 1L;
    String cacheKey = "ingredient_" + ingredientId;
    List<Dish> dishes = new ArrayList<>();
    Dish dish1 = new Dish();
    dish1.setName("Dish 1");
    Dish dish2 = new Dish();
    dish2.setName("Dish 2");
    dishes.add(dish1);
    dishes.add(dish2);

    when(dishCache.containsKey(cacheKey)).thenReturn(false);
    when(dishRepository.findDishesByIngredientList_Id(ingredientId)).thenReturn(dishes);

    List<DishDTO> dishDTOs = dishService.getDishesWithIngredient(ingredientId);

    assertEquals(2, dishDTOs.size());
    assertEquals("Dish 1", dishDTOs.get(0).getName());
    assertEquals("Dish 2", dishDTOs.get(1).getName());

    verify(dishCache, times(1)).putList(cacheKey, dishDTOs);
  }

  @Test
  void testGetByName() throws DishNotFoundException {
    String name = "Test Name";

    when(dishCache.containsKey(name)).thenReturn(true);

    assertDoesNotThrow(() -> dishService.getByName(name));

    when(dishCache.containsKey(name)).thenReturn(false);
    when(dishRepository.findByName(name)).thenReturn(null);

    assertThrows(DishNotFoundException.class, () -> dishService.getByName(name));
  }

  @Test
  void testUpdateDish() throws DishNotFoundException {
    String name = "Test Name";
    Dish dish = new Dish();
    dish.setName(name);

    when(dishRepository.findByName(name)).thenReturn(dish);

    assertDoesNotThrow(() -> dishService.updateDish(name, dish));

    when(dishRepository.findByName(name)).thenReturn(null);

    assertThrows(DishNotFoundException.class, () -> dishService.updateDish(name, dish));
  }

  @Test
  void testDeleteDish() throws DishNotFoundException {
    Long id = 123L;
    Dish dish = new Dish();
    dish.setId(id);

    when(dishRepository.findById(id)).thenReturn(java.util.Optional.of(dish));

    assertDoesNotThrow(() -> dishService.deleteDish(id));

    when(dishRepository.findById(id)).thenReturn(java.util.Optional.empty());

    assertThrows(DishNotFoundException.class, () -> dishService.deleteDish(id));
  }
}
