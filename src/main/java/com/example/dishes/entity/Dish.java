package com.example.dishes.entity;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table
public class Dish {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;
  private String country;
  private String category;
  private String instruction;

  @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
      CascadeType.REMOVE}, mappedBy = "dish")
  private List<Image> imageList = new ArrayList<>();

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "DISH_INGREDIENT_MAPPING",
      joinColumns = @JoinColumn(name = "dishId"),
      inverseJoinColumns = @JoinColumn(name = "ingredientId"))
  private List<Ingredient> ingredientList = new ArrayList<>();

  public Dish() {
    // No initialization logic needed for this constructor
  }

  public Dish(JsonNode jsonNode) {
    this.name = jsonNode.get("strMeal").asText();
    this.country = jsonNode.get("strArea").asText();
    this.category = jsonNode.get("strCategory").asText();
    this.instruction = jsonNode.get("strInstructions").asText();
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getInstruction() {
    return instruction;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public List<Image> getImageList() {
    return imageList;
  }

  public void setImageList(List<Image> imageList) {
    this.imageList = imageList;
  }

  public List<Ingredient> getIngredientList() {
    return ingredientList;
  }

  public void setIngredientList(List<Ingredient> ingredientList) {
    this.ingredientList = ingredientList;
  }
}
