
package com.example.dishes.dto;

import com.example.dishes.entity.Ingredient;

public class IngredientDTO {

    private String name;

    public static IngredientDTO toModel(Ingredient entity){
        IngredientDTO model = new IngredientDTO();
        model.setName(entity.getName());
        return model;
    }

    public IngredientDTO() {
        // No initialization logic needed for this constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}