package com.example.dishes.model;

import com.example.dishes.entity.DishEntity;

public class Dish {
    private String name;
    private String category;
    private String country;
    private String instruction;


    public static Dish toModel(DishEntity entity) {
        Dish model = new Dish();
        model.setName(entity.getName());
        model.setCountry(entity.getCountry());
        model.setCategory(entity.getCategory());
        model.setInstruction(entity.getInstruction());
        return model;
    }


    // Default constructor is empty because the class only contains static methods and does not require instance creation
    public Dish() {
        // Empty constructor
        // This class contains only static methods and does not assume instance creation
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

}
