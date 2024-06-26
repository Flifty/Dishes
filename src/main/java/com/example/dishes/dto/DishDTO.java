package com.example.dishes.dto;

import com.example.dishes.entity.Dish;
import java.util.ArrayList;
import java.util.List;

public class DishDTO {
  private String name;
  private String category;
  private String country;
  private String instruction;
  private List<ImageDTO> imagesList = new ArrayList<>();
  private List<IngredientDTO> ingredientsList = new ArrayList<>();

  public static DishDTO toModel(Dish entity) {
    DishDTO model = new DishDTO();

    model.setName(entity.getName());
    model.setCountry(entity.getCountry());
    model.setCategory(entity.getCategory());
    model.setInstruction(entity.getInstruction());
      if (model.getImagesList() != null) {
          model.setImagesList(entity.getImageList().stream().map(ImageDTO::toModel).toList());
      }
      if (model.getIngredientsList() != null) {
          model.setIngredientsList(
              entity.getIngredientList().stream().map(IngredientDTO::toModel).toList());
      }

    return model;
  }

  public DishDTO() {
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

  public List<ImageDTO> getImagesList() {
    return imagesList;
  }

  public void setImagesList(List<ImageDTO> imageDTOList) {
    this.imagesList = imageDTOList;
  }

  public List<IngredientDTO> getIngredientsList() {
    return ingredientsList;
  }

  public void setIngredientsList(List<IngredientDTO> ingredientsList) {
    this.ingredientsList = ingredientsList;
  }
}
