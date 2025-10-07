package com.DietIA.NextGROUP.Controller.DTO.UserProfileDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record UserProfileDTO(
        @NotNull @Positive Double height,
        @NotNull @Positive Double weight,
        @NotNull @Past LocalDate dateOfBirth,
        @NotNull String gender,
        @NotNull String activityLevel,
        @NotNull String goal,
        String dietaryRestrictions
) {
}
