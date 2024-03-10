package com.example.dishes.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
public class IngredientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "DISH_INGREDIENT_MAPPING",
            joinColumns = @JoinColumn(name  = "ingredientId"),
            inverseJoinColumns = @JoinColumn(name = "dishId"))
    private List<DishEntity> dishList = new ArrayList<>();

    public IngredientEntity() {
        // No initialization logic needed for this constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DishEntity> getDishList() {
        return dishList;
    }

    public void setDishList(List<DishEntity> dishList) {
        this.dishList = dishList;
    }
}
