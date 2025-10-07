package com.DietIA.NextGROUP.Services;

import com.DietIA.NextGROUP.Controller.DTO.UserProfileDTO.UserProfileDTO;
import com.DietIA.NextGROUP.Controller.Mappers.UserProfileMapper;
import com.DietIA.NextGROUP.Model.User;
import com.DietIA.NextGROUP.Model.UserProfile;
import com.DietIA.NextGROUP.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository repository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Transactional(readOnly = true)
    public UserProfileDTO getProfileForCurrentUser() {
        User currentUser = getCurrentUser();
        UserProfile profile = repository.findByUser_Id(currentUser.getId())
                .orElse(new UserProfile(currentUser));


        return userProfileMapper.toDTO(profile);
    }


    @Transactional
    public UserProfileDTO createOrUpdateProfile(UserProfileDTO dto) {
        User currentUser = getCurrentUser();
        UserProfile profile = repository.findByUser_Id(currentUser.getId())
                .orElse(new UserProfile(currentUser));


        userProfileMapper.updateEntityFromDto(dto, profile);

        UserProfile savedProfile = repository.save(profile);


        return userProfileMapper.toDTO(savedProfile);
    }

    // Método auxiliar para pegar o usuário da sessão de segurança
    private User getCurrentUser() {
        // Esta é a forma padrão de pegar o usuário que foi autenticado pelo Spring Security
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
