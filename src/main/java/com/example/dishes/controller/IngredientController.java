package com.example.dishes.controller;

import com.example.dishes.entity.Ingredient;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.exception.IngredientAlreadyExistException;
import com.example.dishes.exception.IngredientNotFoundException;
import com.example.dishes.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

  private final IngredientService ingredientService;
  private final Logger log = LoggerFactory.getLogger(IngredientController.class);

  public IngredientController(IngredientService ingredientService) {
    this.ingredientService = ingredientService;
  }

  private static final String ERROR_MESSAGE = "Произошла ошибка";

  @PostMapping
  public ResponseEntity<String> addIngredient(@RequestParam Long dishId,
                                         @RequestBody Ingredient ingredient) {
    log.info("ingredient post запрос был вызван");
    try {
      ingredientService.addIngredient(dishId, ingredient);
      log.info("Ингредиент был успешно добавлен");
      return ResponseEntity.ok("Ингредиент был успешно добавлен");
    } catch (DishNotFoundException | IngredientAlreadyExistException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @GetMapping
  public ResponseEntity<String> getIngredient(@RequestParam Long id) {
    log.info("ingredient get запрос был вызван");
    try {
      log.info("Ингредиент был успешно получен");
      return ResponseEntity.ok(ingredientService.getIngredient(id));
    } catch (IngredientNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @PutMapping
  public ResponseEntity<String> updateIngredient(@RequestParam Long id,
                                            @RequestBody Ingredient updatedIngredient) {
    log.info("ingredient put запрос был вызван");
    try {
      ingredientService.updateIngredient(id, updatedIngredient);
      log.info("Ингредиент был успешно изменен");
      return ResponseEntity.ok("Ингредиент был успешно изменен");
    } catch (IngredientNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @DeleteMapping
  public ResponseEntity<String> deleteIngredient(@RequestParam Long dishId,
                                            @RequestParam Long ingredientId) {
    log.info("ingredient delete запрос был вызван");
    try {
      ingredientService.deleteIngredient(dishId, ingredientId);
      log.info("Ингредиент был успешно удален");
      return ResponseEntity.ok("Ингредиент был успешно удален");
    } catch (IngredientNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }
}
