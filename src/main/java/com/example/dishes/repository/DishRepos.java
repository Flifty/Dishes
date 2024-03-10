package com.example.dishes.repository;

import com.example.dishes.entity.DishEntity;
import org.springframework.data.repository.CrudRepository;

public interface DishRepos extends CrudRepository<DishEntity, Long> {
    DishEntity findByName(String name);
}
