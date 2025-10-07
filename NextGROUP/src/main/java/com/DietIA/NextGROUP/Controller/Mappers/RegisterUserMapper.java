package com.DietIA.NextGROUP.Controller.Mappers;

import com.DietIA.NextGROUP.Controller.DTO.UserDTO.RegisterUserDTO;
import com.DietIA.NextGROUP.Model.User;
import org.mapstruct.Mapper;

@Mapper
public interface RegisterUserMapper {
    User toEntity(RegisterUserDTO dto);

    RegisterUserDTO toDTO(User user);
}
