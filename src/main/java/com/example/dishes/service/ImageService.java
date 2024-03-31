package com.example.dishes.service;

import com.example.dishes.dto.ImageDTO;
import com.example.dishes.entity.Dish;
import com.example.dishes.entity.Image;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.exception.ImageAlreadyExistException;
import com.example.dishes.exception.ImageNotFoundException;
import com.example.dishes.repository.DishRepository;
import com.example.dishes.repository.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ImageService {

  private final ImageRepository imageRepository;
  private final DishRepository dishRepository;
  private static final String IMAGE_NOT_FOUND_STRING = "Изображение не найдено";

  @Autowired
  public ImageService(ImageRepository imageRepository, DishRepository dishRepository) {
    this.imageRepository = imageRepository;
    this.dishRepository = dishRepository;
  }

  public void addImage(Long id, Image image)
      throws DishNotFoundException, ImageAlreadyExistException {
    Dish dish = dishRepository.findById(id).orElse(null);
    if (dish != null) {
      image.setDish(dish);
      if (imageRepository.findByPicture(image.getPicture()) != null) {
        throw new ImageAlreadyExistException("Такое изображение уже существует");
      }
      imageRepository.save(image);
    } else {
      throw new DishNotFoundException("Не удалось добавить изображение. Блюдо не найдено");
    }
  }

  public ImageDTO getImage(Long id) throws ImageNotFoundException {
    Image image = imageRepository.findById(id).orElse(null);
    if (image != null) {
      return ImageDTO.toModel(image);
    } else {
      throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
    }
  }

  public void updateImage(Long id, Image image) throws ImageNotFoundException {
    Image imageEntity = imageRepository.findById(id).orElse(null);
    if (imageEntity != null) {
      imageEntity.setPicture(image.getPicture());
      imageRepository.save(imageEntity);
    } else {
      throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
    }
  }

  @Transactional
  public void deleteImage(Long id) throws ImageNotFoundException {
    Image image = imageRepository.findById(id).orElse(null);
    if (image != null) {
      image.getDish().getImageList().remove(image);
      dishRepository.save(image.getDish());
      imageRepository.deleteById(id);
    } else {
      throw new ImageNotFoundException(IMAGE_NOT_FOUND_STRING);
    }
  }
}
