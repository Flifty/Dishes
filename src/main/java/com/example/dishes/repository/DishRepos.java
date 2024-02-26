package com.example.dishes.repository;

import com.example.dishes.entity.DishEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepos extends CrudRepository<DishEntity, Long> {
    List<DishEntity> findByName(String name);
}
