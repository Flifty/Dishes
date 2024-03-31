package com.example.dishes.controller;

import com.example.dishes.dto.DishDTO;
import com.example.dishes.entity.Dish;
import com.example.dishes.exception.DishAlreadyExistException;
import com.example.dishes.exception.DishExceptionHandler;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.service.DishService;
import java.util.List;
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
import org.springframework.web.client.HttpClientErrorException;


@RequestMapping("/dishes")
public class DishController {

  private static final String ERROR_MESSAGE = "Произошла ошибка";
  private static final String GETTING_SUCCESS_MESSAGE = "Блюдо было успешно получено";
  private final DishService dishService;
  private final Logger log = LoggerFactory.getLogger(DishController.class);
  final DishExceptionHandler dishExceptionHandler;

  public DishController(DishService dishService, DishExceptionHandler dishExceptionHandler) {
    this.dishService = dishService;
    this.dishExceptionHandler = dishExceptionHandler;
  }

  @PostMapping
  public ResponseEntity<String> addDish(@RequestBody Dish dish) {
    log.info("dish post запрос был вызван");
    try {
      dishService.addDish(dish);
      log.info("Блюдо было успешно добавлено");
      return ResponseEntity.ok("Блюдо было успешно добавлено");
    } catch (DishAlreadyExistException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @GetMapping
  public ResponseEntity<String> getDish(@RequestParam(required = false) String name) {
    log.info("dish get запрос был вызван");
    try {
      log.info(GETTING_SUCCESS_MESSAGE);
      return ResponseEntity.ok(dishService.getDish(name));
    } catch (DishNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @GetMapping("/with-ingredient")
  public ResponseEntity<String> getDishesWithIngredient(@RequestParam Long ingredientId) {
    log.info("dish get /with-ingredient запрос был вызван");
    try {
      List<DishDTO> dishes = dishService.getDishesWithIngredient(ingredientId);
      log.info(GETTING_SUCCESS_MESSAGE);
      return ResponseEntity.ok(dishes);
    } catch (DishNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @GetMapping("/name")
  public ResponseEntity<String> getDishByName(@RequestParam String name) {
    log.info("dish get /name запрос был вызван");
    try {
      List<DishDTO> dishDto = dishService.getByName(name);
      log.info(GETTING_SUCCESS_MESSAGE);
      return ResponseEntity.ok(dishDto);
    } catch (DishNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @PutMapping
  public ResponseEntity<String> updateDish(@RequestParam String name, @RequestBody Dish updatedDish) {
    log.info("dish put запрос был вызван");
    try {
      dishService.updateDish(name, updatedDish);
      log.info("Блюдо было успешно изменено");
      return ResponseEntity.ok("Блюдо было успешно изменено");
    } catch (DishNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }

  @DeleteMapping
  public ResponseEntity<String> deleteDish(@RequestParam Long id) {
    log.info("dish delete запрос был вызван");
    try {
      dishService.deleteDish(id);
      log.info("Блюдо было успешно удалено");
      return ResponseEntity.ok("Блюдо было успешно удалено");
    } catch (DishNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ERROR_MESSAGE);
    }
  }
}
