package com.example.dishes.exception;

public class DishAlreadyExistException extends Exception {
  public DishAlreadyExistException(String message) {
    super(message);
  }
}
