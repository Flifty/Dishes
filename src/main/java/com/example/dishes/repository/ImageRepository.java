package com.example.dishes.repository;

import com.example.dishes.entity.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
  Image findByPicture(String picture);
}
