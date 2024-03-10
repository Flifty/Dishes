package com.example.dishes.repository;

import com.example.dishes.entity.ImageEntity;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepos extends CrudRepository<ImageEntity, Long> {
    ImageEntity findByPicture(String picture);
}
