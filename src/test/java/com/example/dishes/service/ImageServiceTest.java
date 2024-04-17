package com.example.dishes.service;

import com.example.dishes.entity.Dish;
import com.example.dishes.entity.Image;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.exception.ImageAlreadyExistException;
import com.example.dishes.exception.ImageNotFoundException;
import com.example.dishes.repository.DishRepository;
import com.example.dishes.repository.ImageRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageServiceTest {

  @Mock
  private ImageRepository imageRepository;

  @Mock
  private DishRepository dishRepository;

  @InjectMocks
  private ImageService imageService;

  private Dish dish;
  private Image image;

  @BeforeEach
  void setUp() {
    dish = new Dish();
    image = new Image();
  }

  @Test
  void testAddImage_Success() throws DishNotFoundException, ImageAlreadyExistException {
    when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
    when(imageRepository.findByPicture(anyString())).thenReturn(null);

    assertDoesNotThrow(() -> imageService.addImage(1L, image));

    verify(imageRepository, times(1)).save(image);
  }

  @Test
  void testAddImage_DishNotFound() {
    assertThrows(DishNotFoundException.class, () -> imageService.addImage(1L, image));
  }

  @Test
  void testAddImage_ImageAlreadyExist() {
    Dish dish = new Dish();
    dish.setId(1L);
    Image image = new Image();
    image.setPicture("existing_picture");
    image.setDish(dish);

    when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
    when(imageRepository.findByPicture("existing_picture")).thenReturn(image);

    assertThrows(ImageAlreadyExistException.class, () -> imageService.addImage(1L, image));
  }

  @Test
  void testGetImage_Success() throws ImageNotFoundException {
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
    assertNotNull(imageService.getImage(1L));
  }

  @Test
  void testGetImage_ImageNotFound() {
    assertThrows(ImageNotFoundException.class, () -> imageService.getImage(1L));
  }

  @Test
  void testUpdateImage_Success() throws ImageNotFoundException {
    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
    Image updatedImage = new Image();
    updatedImage.setPicture("new_picture");
    assertDoesNotThrow(() -> imageService.updateImage(1L, updatedImage));
    assertEquals("new_picture", image.getPicture());
  }

  @Test
  void testUpdateImage_ImageNotFound() {
    assertThrows(ImageNotFoundException.class, () -> imageService.updateImage(1L, new Image()));
  }

  @Test
  void testDeleteImage_Success() throws ImageNotFoundException {
    Dish dish = new Dish();
    dish.setId(1L);
    dish.setImageList(new ArrayList<>());

    Image image = new Image();
    image.setId(1L);
    image.setDish(dish);
    dish.getImageList().add(image);

    when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

    assertDoesNotThrow(() -> imageService.deleteImage(1L));
    verify(imageRepository, times(1)).deleteById(1L);
  }


  @Test
  void testDeleteImage_ImageNotFound() {
    assertThrows(ImageNotFoundException.class, () -> imageService.deleteImage(1L));
  }
}

