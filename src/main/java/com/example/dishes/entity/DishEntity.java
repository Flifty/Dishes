package com.example.dishes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String country;
    private String category;
    private String instruction;

    public DishEntity() {
    }

    public DishEntity(JsonNode jsonNode) {
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

}
