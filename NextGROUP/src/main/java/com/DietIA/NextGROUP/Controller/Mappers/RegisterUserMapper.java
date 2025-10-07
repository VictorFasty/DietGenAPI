package com.DietIA.NextGROUP.Controller.Mappers;

import com.DietIA.NextGROUP.Controller.DTO.UserDTO.RegisterUserDTO;
import com.DietIA.NextGROUP.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(RegisterUserDTO dto);

    RegisterUserDTO toDTO(User user);
}
