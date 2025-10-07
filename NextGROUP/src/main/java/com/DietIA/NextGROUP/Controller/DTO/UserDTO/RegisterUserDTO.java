package com.DietIA.NextGROUP.Controller.DTO.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(
    @NotNull
    String name,
    @Email(message = "Formato de email inválido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Formato de email inválido"
    )
    @NotNull
    String email,
    @NotNull
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 50 caracteres")
    String password
) {
}
