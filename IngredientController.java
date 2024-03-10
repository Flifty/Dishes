package com.example.dishes.controller;

import com.example.dishes.entity.IngredientEntity;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.exception.IngredientAlreadyExistException;
import com.example.dishes.exception.IngredientNotFoundException;
import com.example.dishes.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    private static final String ERROR_MESSAGE = "Произошла ошибка";

    @PostMapping
    public ResponseEntity<?> addIngredient(@RequestParam Long dishId, @RequestBody IngredientEntity ingredient) {
        try {
            ingredientService.addIngredient(dishId, ingredient);
            return ResponseEntity.ok("Ингредиент был успешно сохранен");
        } catch (DishNotFoundException | IngredientAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping
    public ResponseEntity<?> getIngredient(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(ingredientService.getIngredient(id));
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateIngredient(@RequestParam Long id, @RequestBody IngredientEntity updatedIngredient) {
        try {
            ingredientService.updateIngredient(id, updatedIngredient);
            return ResponseEntity.ok("Ингредиент был успешно изменен");
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteIngredient(@RequestParam Long dishId, @RequestParam Long ingredientId) {
        try {
            ingredientService.deleteIngredient(dishId, ingredientId);
            return ResponseEntity.ok("Ингредиент был успешно удален");
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }
}
