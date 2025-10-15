package com.DietIA.NextGROUP.Controller.DTO.DietPlanDTO;

import java.time.LocalDate;
import java.util.UUID;

public record DietPlanDTO(


        UUID id,
        LocalDate createdAt
) {
}
