package com.DietIA.NextGROUP.Controller.DTO.UserProfileDTO;

public record FoodItemDTO(
        String foodName,
        Double quantityGrams,
        Double calories,
        Double protein,
        Double carbs,
        Double fat
) {}
