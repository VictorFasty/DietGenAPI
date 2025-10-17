package com.DietIA.NextGROUP.Controller.Mappers;

import com.DietIA.NextGROUP.Controller.DTO.DietPlanDTO.DietPlanDTO;
import com.DietIA.NextGROUP.Controller.DTO.DietPlanDTO.DietPlanResponseDTO;
import com.DietIA.NextGROUP.Controller.DTO.UserDTO.RegisterUserDTO;
import com.DietIA.NextGROUP.Model.DietPlan;
import com.DietIA.NextGROUP.Model.FoodItem;
import com.DietIA.NextGROUP.Model.Meal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DietPlanMapper {
    DietPlanResponseDTO toDto(DietPlan dietPlan);

    DietPlanResponseDTO.MealDTO toDto(Meal meal);

    DietPlanResponseDTO.FoodItemDTO toDto(FoodItem foodItem);
}
