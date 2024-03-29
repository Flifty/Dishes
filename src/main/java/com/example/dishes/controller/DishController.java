package com.example.dishes.controller;

import com.example.dishes.entity.Dish;
import com.example.dishes.exception.DishAlreadyExistException;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.dto.DishDTO;
import com.example.dishes.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/dishes")
public class DishController {

    private static final String ERROR_MESSAGE = "Произошла ошибка";
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<?> addDish(@RequestBody Dish dish) {
        try {
            dishService.addDish(dish);
            return ResponseEntity.ok("Блюдо было успешно сохранено");
        } catch (DishAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping
    public ResponseEntity<?> getDish(@RequestParam(required = false) String name) {
        try {
            return ResponseEntity.ok(dishService.getDish(name));
        } catch (DishNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/with-ingredient")
    public ResponseEntity<?> getDishesWithIngredient(@RequestParam Long ingredientId) {
        try {
            List<DishDTO> dishes = dishService.getDishesWithIngredient(ingredientId);
            return ResponseEntity.ok(dishes);
        } catch (DishNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/name")
    public ResponseEntity<?> getDishByName(@RequestParam String name) {
        try {
            List<DishDTO> dishDTOS = dishService.getByName(name);
            return ResponseEntity.ok(dishDTOS);
        } catch (DishNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDish(@RequestParam String name, @RequestBody Dish updatedDish) {
        try {
            dishService.updateDish(name, updatedDish);
            return ResponseEntity.ok("Блюдо было успешно изменено");
        } catch (DishNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDish(@RequestParam Long id) {
        try {
            dishService.deleteDish(id);
            return ResponseEntity.ok("Блюдо было успешно удалено");
        } catch (DishNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }
}
