package com.example.dishes.service;

import com.example.dishes.entity.DishEntity;
import com.example.dishes.exception.DishAlreadyExistException;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.model.Dish;
import com.example.dishes.repository.DishRepos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {

    private final ObjectMapper objectMapper;
    private final DishRepos dishRepos;
    private static final String MEALS_STRING = "meals";

    @Autowired
    public DishService(ObjectMapper objectMapper, DishRepos dishRepos) {
        this.objectMapper = objectMapper;
        this.dishRepos = dishRepos;
    }

    public void addDish(DishEntity dish) throws DishAlreadyExistException {
        if (dishRepos.findByName(dish.getName()) != null) {
            throw new DishAlreadyExistException("Такое блюдо уже существует");
        }
        dishRepos.save(dish);
    }

    public Dish getDish(String name) throws DishNotFoundException {
        DishEntity dish = dishRepos.findByName(name);
        if (dish == null) {
            throw new DishNotFoundException("Блюдо не найдено");
        }
        return Dish.toModel(dish);
    }

    public List<Dish> getByName(String name) throws DishNotFoundException, JsonProcessingException {

        String apiUrl = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + URLEncoder.encode(name, StandardCharsets.UTF_8);

        RestTemplate restTemplate = new RestTemplate();

        String jsonString = restTemplate.getForObject(apiUrl, String.class);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = mapper.readTree(jsonString);

        if (jsonNode.has(MEALS_STRING) && jsonNode.get(MEALS_STRING).isArray() && !jsonNode.get(MEALS_STRING).isEmpty()) {
            List<Dish> dishes = new ArrayList<>();
            for (JsonNode mealNode : jsonNode.get(MEALS_STRING)) {
                DishEntity dishEntity = new DishEntity(mealNode);
                dishes.add(Dish.toOldModel(dishEntity));
            }
            return dishes;
        } else {
            throw new DishNotFoundException("Блюдо с таким названием не было найдено.");
        }
    }

    public void updateDish(String name, DishEntity dish) throws DishNotFoundException {
        DishEntity dishEntity = dishRepos.findByName(name);
        if (dishEntity == null) {
            throw new DishNotFoundException("Блюдо не найдено");
        }
        dishEntity.setName(dish.getName());
        dishEntity.setCountry(dish.getCountry());
        dishEntity.setCategory(dish.getCategory());
        dishEntity.setInstruction(dish.getInstruction());
        dishRepos.save(dishEntity);
    }

    public void deleteDish(Long id) throws DishNotFoundException {
        DishEntity dishEntity = dishRepos.findById(id).orElse(null);
        if (dishEntity != null) {
            dishRepos.deleteById(id);
        } else {
            throw new DishNotFoundException("Блюдо не найдено");
        }
    }
}
