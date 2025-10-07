package com.DietIA.NextGROUP.Controller.Mappers;

import com.DietIA.NextGROUP.Controller.DTO.UserProfileDTO.UserProfileDTO;
import com.DietIA.NextGROUP.Model.UserProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toEntity(UserProfileDTO dto);

    UserProfileDTO toDTO(UserProfile userProfile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserProfileDTO dto, @MappingTarget UserProfile userProfile);

}
