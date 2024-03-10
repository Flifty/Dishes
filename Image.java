package com.example.dishes.model;

import com.example.dishes.entity.ImageEntity;

public class Image {
    private String picture;

    public static Image toModel(ImageEntity entity){
        Image model = new Image();
        model.setPicture(entity.getPicture());
        return model;
    }

    public Image() {
        // No initialization logic needed for this constructor
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
