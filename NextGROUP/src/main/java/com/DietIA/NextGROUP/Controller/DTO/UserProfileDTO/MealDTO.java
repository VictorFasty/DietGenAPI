package com.DietIA.NextGROUP.Controller.DTO.UserProfileDTO;

import java.util.List;

public record MealDTO(
        String name,
        List<FoodItemDTO> items // A lista de "ingredientes" de cada prato
) {}