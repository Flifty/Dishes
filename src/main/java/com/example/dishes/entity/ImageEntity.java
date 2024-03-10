package com.example.dishes.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String picture;

    @ManyToOne
    @JoinColumn(name = "dishId")
    private DishEntity dish;

    public ImageEntity() {
    }

    public ImageEntity(JsonNode jsonNode) {
        this.picture = jsonNode.get("strMealThumb").asText();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public DishEntity getDish() {
        return dish;
    }

    public void setDish(DishEntity dish) {
        this.dish = dish;
    }
}
