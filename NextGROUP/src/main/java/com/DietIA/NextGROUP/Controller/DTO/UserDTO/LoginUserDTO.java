package com.DietIA.NextGROUP.Controller.DTO.UserDTO;

import jakarta.validation.constraints.*;

public record LoginUserDTO(
        @Email(message = "Formato de email inválido")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Formato de email inválido"
        )
        @NotNull
        String email,
        @NotNull
        @NotBlank
        String password
) {
}
