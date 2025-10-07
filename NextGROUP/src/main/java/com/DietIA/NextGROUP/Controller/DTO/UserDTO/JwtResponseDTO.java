
package com.DietIA.NextGROUP.Controller.DTO.UserDTO;

import jakarta.validation.constraints.NotNull;

public record JwtResponseDTO(
        @NotNull
        String Token
) {
}
