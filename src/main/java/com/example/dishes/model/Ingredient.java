
package com.example.dishes.model;

import com.example.dishes.entity.IngredientEntity;

public class Ingredient {

    private String name;

    public static Ingredient toModel(IngredientEntity entity){
        Ingredient model = new Ingredient();
        model.setName(entity.getName());
        return model;
    }

    public Ingredient() {
        // No initialization logic needed for this constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}