package com.example.dishes.service;

import com.example.dishes.entity.Dish;
import com.example.dishes.entity.Ingredient;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.exception.IngredientAlreadyExistException;
import com.example.dishes.exception.IngredientNotFoundException;
import com.example.dishes.repository.DishRepository;
import com.example.dishes.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class IngredientServiceTest {

  @Mock
  private IngredientRepository ingredientRepository;

  @Mock
  private DishRepository dishRepository;

  @InjectMocks
  private IngredientService ingredientService;

  private Dish dish;
  private Ingredient ingredient;

  @BeforeEach
  void setUp() {
    dish = new Dish();
    ingredient = new Ingredient();
  }

  @Test
  void testAddIngredient_Success() throws DishNotFoundException, IngredientAlreadyExistException {
    when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
    when(ingredientRepository.findByName(anyString())).thenReturn(null);

    assertDoesNotThrow(() -> ingredientService.addIngredient(1L, ingredient));

    verify(ingredientRepository, times(1)).save(ingredient);
  }

  @Test
  void testAddIngredient_DishNotFound() {
    assertThrows(DishNotFoundException.class, () -> ingredientService.addIngredient(1L, ingredient));
  }

  @Test
  void testAddIngredient_IngredientAlreadyExist() {
    Ingredient existingIngredient = new Ingredient();
    existingIngredient.setName("Salt");
    dish.getIngredientList().add(existingIngredient);

    when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
    when(ingredientRepository.findByName(existingIngredient.getName())).thenReturn(existingIngredient);

    assertThrows(IngredientAlreadyExistException.class, () -> ingredientService.addIngredient(1L, existingIngredient));
  }


  @Test
  void testGetIngredient_Success() throws IngredientNotFoundException {
    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
    assertNotNull(ingredientService.getIngredient(1L));
  }

  @Test
  void testGetIngredient_IngredientNotFound() {
    assertThrows(IngredientNotFoundException.class, () -> ingredientService.getIngredient(1L));
  }

  @Test
  void testUpdateIngredient_Success() throws IngredientNotFoundException {
    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));
    Ingredient updatedIngredient = new Ingredient();
    updatedIngredient.setName("new_name");
    assertDoesNotThrow(() -> ingredientService.updateIngredient(1L, updatedIngredient));
    assertEquals("new_name", ingredient.getName());
  }

  @Test
  void testUpdateIngredient_IngredientNotFound() {
    assertThrows(IngredientNotFoundException.class, () -> ingredientService.updateIngredient(1L, new Ingredient()));
  }

  @Test
  void testDeleteIngredient_Success() throws IngredientNotFoundException, DishNotFoundException {
    Dish dish = new Dish();
    dish.setId(1L);
    Ingredient ingredient = new Ingredient();
    ingredient.setId(1L);
    dish.getIngredientList().add(ingredient);

    when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

    ingredientService.deleteIngredient(1L, 1L);

    verify(dishRepository, times(1)).save(dish);
    verify(ingredientRepository, times(1)).save(ingredient);
    verify(ingredientRepository, never()).deleteById(anyLong());
    assertThat(dish.getIngredientList()).doesNotContain(ingredient);
  }

  @Test
  void testDeleteIngredient_IngredientNotFound() {
    assertThrows(IngredientNotFoundException.class, () -> ingredientService.deleteIngredient(1L, 1L));
  }

  @Test
  void testDeleteIngredient_DishNotFound() {
    Dish dish = new Dish();
    dish.setId(1L);
    when(dishRepository.findById(1L)).thenReturn(Optional.empty());
    when(ingredientRepository.findById(1L)).thenReturn(Optional.of(new Ingredient()));

    assertThrows(DishNotFoundException.class, () -> ingredientService.deleteIngredient(1L, 1L));
  }
}


