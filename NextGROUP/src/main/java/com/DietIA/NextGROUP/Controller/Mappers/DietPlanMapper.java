package com.DietIA.NextGROUP.Controller.Mappers;

import com.DietIA.NextGROUP.Controller.DTO.DietPlanDTO.DietPlanDTO;
import com.DietIA.NextGROUP.Controller.DTO.UserDTO.RegisterUserDTO;
import com.DietIA.NextGROUP.Model.DietPlan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DietPlanMapper {
    DietPlan toEntity(DietPlanDTO dto);

    DietPlanDTO toDTO(DietPlan user);
}
