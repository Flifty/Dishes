package com.example.dishes.model;

import com.example.dishes.entity.DishEntity;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private String name;
    private String category;
    private String country;
    private String instruction;
    private List<Image> imagesList = new ArrayList<>();
    private List<Ingredient> ingredientsList = new ArrayList<>();

    public static Dish toModel(DishEntity entity) {
        Dish model = new Dish();

        model.setName(entity.getName());
        model.setCountry(entity.getCountry());
        model.setCategory(entity.getCategory());
        model.setInstruction(entity.getInstruction());
        if(model.getImagesList() != null)
            model.setImagesList(entity.getImageList().stream().map(Image::toModel).toList());
        if(model.getIngredientsList() != null)
            model.setIngredientsList(entity.getIngredientList().stream().map(Ingredient::toModel).toList());

        return model;
    }

    public static Dish toOldModel(DishEntity entity) {
        Dish oldModel = new Dish();

        oldModel.setName(entity.getName());
        oldModel.setCountry(entity.getCountry());
        oldModel.setCategory(entity.getCategory());
        oldModel.setInstruction(entity.getInstruction());

        return oldModel;
    }

    public Dish() {
        // No initialization logic needed for this constructor
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

    public List<Image> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<Image> imageList) {
        this.imagesList = imageList;
    }

    public List<Ingredient> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<Ingredient> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }
}
