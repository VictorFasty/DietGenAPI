package com.DietIA.NextGROUP.Controller.DTO.DietPlanDTO;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public record DietPlanResponseDTO(
        UUID id,
        LocalDate createdAt,
        List<MealDTO> meals // A lista de refeições
) {

    public record MealDTO(
            String name,
            List<FoodItemDTO> items
    ) {}

    public record FoodItemDTO(
            String foodName

    ) {}
}