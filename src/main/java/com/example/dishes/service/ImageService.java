package com.example.dishes.service;

import com.example.dishes.entity.DishEntity;
import com.example.dishes.entity.ImageEntity;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.exception.ImageAlreadyExistException;
import com.example.dishes.exception.ImageNotFoundException;
import com.example.dishes.model.Image;
import com.example.dishes.repository.DishRepos;
import com.example.dishes.repository.ImageRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


@Service
public class ImageService {

    private final ImageRepos imageRepos;
    private final DishRepos dishRepos;
    private static final String IMAGE_NOT_FOUND_STRING = "Изображение не найдено";

    @Autowired
    public ImageService(ImageRepos imageRepos, DishRepos dishRepos) {
        this.imageRepos = imageRepos;
        this.dishRepos = dishRepos;
    }

    public void addImage(Long id, ImageEntity image) throws DishNotFoundException, ImageAlreadyExistException {
        DishEntity dish = dishRepos.findById(id).orElse(null);
        if (dish != null) {
            image.setDish(dish);
            if (imageRepos.findByPicture(image.getPicture()) != null) {
                throw new ImageAlreadyExistException("Такое изображение уже существует");
            }
            imageRepos.save(image);
        } else {
            throw new DishNotFoundException("Не удалось добавить изображение. Блюдо не найдено");
        }
    }

    public Image getImage(Long id) throws ImageNotFoundException {
        ImageEntity image = imageRepos.findById(id).orElse(null);
        if (image != null) {
            return Image.toModel(image);
        } else {
            throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
        }
    }

    public void updateImage(Long id, ImageEntity image) throws ImageNotFoundException {
        ImageEntity imageEntity = imageRepos.findById(id).orElse(null);
        if (imageEntity != null) {
            imageEntity.setPicture(image.getPicture());
            imageRepos.save(imageEntity);
        } else {
            throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
        }
    }

    @Transactional
    public void deleteImage(Long id) throws ImageNotFoundException {
        ImageEntity imageEntity = imageRepos.findById(id).orElse(null);
        if (imageEntity != null) {
            imageEntity.getDish().getImageList().remove(imageEntity);
            dishRepos.save(imageEntity.getDish());
            imageRepos.deleteById(id);
        } else {
            throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
        }
    }
}
