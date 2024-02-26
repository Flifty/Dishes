package com.example.dishes.service;

import com.example.dishes.entity.DishEntity;
import com.example.dishes.exception.DishAlreadyExistExeption;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DishService {

    private final ObjectMapper objectMapper;
    private final DishRepos dishRepos;
    private static final Logger logger = LoggerFactory.getLogger(DishService.class);

    @Autowired
    public DishService(ObjectMapper objectMapper, DishRepos dishRepos) {
        this.objectMapper = objectMapper;
        this.dishRepos = dishRepos;
    }

    public DishEntity add(DishEntity dish) throws DishAlreadyExistExeption {
        List<DishEntity> dishes = dishRepos.findByName(dish.getName());
        if (!dishes.isEmpty()) {
            throw new DishAlreadyExistExeption("Блюдо с таким названием уже существует");
        }
        return dishRepos.save(dish);
    }

    public List<Dish> getFromDb(String name) throws DishNotFoundException {
        List<DishEntity> dishList = dishRepos.findByName(name);

        if (dishList.isEmpty()) {
            throw new DishNotFoundException("Блюдо с таким названием не было найдено");
        }

        return dishList.stream()
                .map(Dish::toModel)
                .toList();
    }

    public List<Dish> getByName(String name) throws DishNotFoundException, DishAlreadyExistExeption, JsonProcessingException {

        String apiUrl = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + URLEncoder.encode(name, StandardCharsets.UTF_8);

        RestTemplate restTemplate = new RestTemplate();

        String jsonString = restTemplate.getForObject(apiUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(jsonString);

        if (jsonNode.has("meals") && jsonNode.get("meals").isArray() && jsonNode.get("meals").size() > 0) {
            List<Dish> dishes = new ArrayList<>();
            for (JsonNode mealNode : jsonNode.get("meals")) {
                DishEntity dishEntity = new DishEntity(mealNode);
                dishes.add(Dish.toModel(dishEntity));
            }
            return dishes;
        } else {
            throw new DishNotFoundException("Блюдо с таким названием не было найдено.");
        }
    }

    public Long delete(Long id) {
       dishRepos.deleteById(id);
       return id;
    }

    public DishEntity processJson(String jsonResponse) {
        try {
            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                return objectMapper.readValue(jsonResponse, DishEntity.class);
            } else {
                logger.warn("JSON-ответ пустой или имеет некорректный формат");
                return null;
            }
        } catch (Exception e) {
            logger.error("Ошибка при обработке JSON", e);
            return null;
        }
    }

}
