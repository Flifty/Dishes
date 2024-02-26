package com.example.dishes.controller;

import com.example.dishes.entity.DishEntity;
import com.example.dishes.exception.DishAlreadyExistExeption;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.model.Dish;
import com.example.dishes.service.DishService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    @Value("${dishbit.api-key}")
    private String apiKey;

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity addDish(@RequestBody DishEntity dish) {
        try {
            dishService.add(dish);
            return ResponseEntity.ok("Блюдо было успешно сохранено");
        } catch (DishAlreadyExistExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping
    public ResponseEntity getDishFromDb(@RequestParam(required = false) String name) {
        try {
            return ResponseEntity.ok(dishService.getFromDb(name));
        } catch (DishNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/name")
    public ResponseEntity<?> getDishByName(@RequestParam String name) {
        try {
            List<Dish> dishes = dishService.getByName(name);
            return ResponseEntity.ok(dishes);
        /*} catch (DishAlreadyExistExeption e) {
            return ResponseEntity.badRequest().body(e.getMessage());*/
        } catch (DishNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDish(@PathVariable Long id) {
        try {
            dishService.delete(id);
            return ResponseEntity.ok("Блюдо было успешно удалено");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

}
