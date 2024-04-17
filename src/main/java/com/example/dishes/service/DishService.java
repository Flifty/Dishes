package com.example.dishes.service;

import com.example.dishes.component.Cache;
import com.example.dishes.dto.DishDTO;
import com.example.dishes.entity.Dish;
import com.example.dishes.exception.DishAlreadyExistException;
import com.example.dishes.exception.DishNotFoundException;
import com.example.dishes.repository.DishRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Component
public class DishService {

  private final ObjectMapper objectMapper;
  private final DishRepository dishRepository;
  private final Cache<String, DishDTO> dishCache;
  private static final String MEALS_STRING = "meals";
  private static final String DISH_NOT_FOUND_STRING = "Блюдо не найдено";

  @Autowired
  public DishService(ObjectMapper objectMapper, DishRepository dishRepository,
                     Cache<String, DishDTO> dishCache) {
    this.objectMapper = objectMapper;
    this.dishRepository = dishRepository;
    this.dishCache = dishCache;
  }

  public void addDish(Dish dish) throws DishAlreadyExistException {
    if (dishRepository.findByName(dish.getName()) != null) {
      throw new DishAlreadyExistException("Такое блюдо уже существует");
    }
    dishRepository.save(dish);
  }

  public List<String> addDishesBulk(List<Dish> dishes) {
    List<String> errors = new ArrayList<>();

    dishes.stream()
        .filter(dish -> dishRepository.findByName(dish.getName()) != null)
        .forEach(dish -> errors.add("Блюдо '" + dish.getName() + "' уже существует"));

    dishes.stream()
        .filter(dish -> dishRepository.findByName(dish.getName()) == null)
        .forEach(dish -> dishRepository.save(dish));

    return errors;
  }

  public DishDTO getDish(String name) throws DishNotFoundException {
    if (dishCache.containsKey(name)) {
      return dishCache.get(name);
    } else {
      Dish dish = dishRepository.findByName(name);
      if (dish == null) {
        throw new DishNotFoundException(DISH_NOT_FOUND_STRING);
      }
      DishDTO dishDTO = DishDTO.toModel(dish);
      dishCache.put(name, dishDTO);
      return dishDTO;
    }
  }

  public List<DishDTO> getDishesWithIngredient(Long ingredientId) throws DishNotFoundException {
    String cacheKey = "ingredient_" + ingredientId;
    if (dishCache.containsKey(cacheKey)) {
      return dishCache.getList(cacheKey);
    } else {
      List<Dish> dishes = dishRepository.findDishesByIngredientList_Id(ingredientId);
      if (dishes.isEmpty()) {
        throw new DishNotFoundException(DISH_NOT_FOUND_STRING);
      }
      List<DishDTO> dishDTOs = dishes.stream().map(DishDTO::toModel).toList();
      dishCache.putList(cacheKey, dishDTOs);
      return dishDTOs;
    }
  }

  public List<DishDTO> getByName(String name)
      throws DishNotFoundException, JsonProcessingException {

    if (dishCache.containsKey(name)) {
      return dishCache.getList(name);
    } else {
      String apiUrl = "https://www.themealdb.com/api/json/v1/1/search.php?s="
          + URLEncoder.encode(name, StandardCharsets.UTF_8);

      RestTemplate restTemplate = new RestTemplate();

      String jsonString = restTemplate.getForObject(apiUrl, String.class);

      ObjectMapper mapper = new ObjectMapper();

      JsonNode jsonNode = mapper.readTree(jsonString);

      if (jsonNode.has(MEALS_STRING) && jsonNode.get(MEALS_STRING).isArray()
          && !jsonNode.get(MEALS_STRING).isEmpty()) {
        List<DishDTO> dishDTOS = new ArrayList<>();
        for (JsonNode mealNode : jsonNode.get(MEALS_STRING)) {
          Dish dish = new Dish(mealNode);
          dishDTOS.add(DishDTO.toModel(dish));
        }
        return dishDTOS;
      } else {
        throw new DishNotFoundException(DISH_NOT_FOUND_STRING);
      }
    }
  }

  public void updateDish(String name, Dish dish) throws DishNotFoundException {
    Dish dishEntity = dishRepository.findByName(name);
    if (dishEntity == null) {
      throw new DishNotFoundException(DISH_NOT_FOUND_STRING);
    }
    dishEntity.setName(dish.getName());
    dishEntity.setCountry(dish.getCountry());
    dishEntity.setCategory(dish.getCategory());
    dishEntity.setInstruction(dish.getInstruction());
    dishRepository.save(dishEntity);
  }

  public void deleteDish(Long id) throws DishNotFoundException {
    Dish dish = dishRepository.findById(id).orElse(null);
    if (dish != null) {
      dishRepository.deleteById(id);
    } else {
      throw new DishNotFoundException(DISH_NOT_FOUND_STRING);
    }
  }
}
