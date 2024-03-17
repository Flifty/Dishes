package com.example.dishes.repository;

import com.example.dishes.entity.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Long> {
    Dish findByName(String name);

    @Query("SELECT d FROM Dish d JOIN d.ingredientList i WHERE i.id = :ingredientId")
    List<Dish> findDishesByIngredientList_Id(Long ingredientId);
}
