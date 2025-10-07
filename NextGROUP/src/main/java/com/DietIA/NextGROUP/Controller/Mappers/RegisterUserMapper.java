package com.DietIA.NextGROUP.Controller;

import com.DietIA.NextGROUP.Controller.DTO.UserDTO.RegisterUserDTO;
import com.DietIA.NextGROUP.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.control.MappingControl;

@Mapper
public interface RegisterUserMapper {
    User toEntity(RegisterUserDTO dto);

    RegisterUserDTO toDTO(User user);
}
