package com.DietIA.NextGROUP.Controller;

import com.DietIA.NextGROUP.Controller.DTO.UserProfileDTO.UserProfileDTO;
import com.DietIA.NextGROUP.Services.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    // Endpoint para buscar o perfil do usuário logado
    @GetMapping("/me") // "/me" é uma convenção para "os dados do usuário atual"
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        UserProfileDTO profileDto = userProfileService.getProfileForCurrentUser();
        return ResponseEntity.ok(profileDto);
    }

    // Endpoint para criar ou atualizar o perfil
    @PutMapping("/me")
    public ResponseEntity<UserProfileDTO> updateMyProfile(@RequestBody @Valid UserProfileDTO profileDto) {
        UserProfileDTO updatedProfile = userProfileService.createOrUpdateProfile(profileDto);
        return ResponseEntity.ok(updatedProfile);
    }
}
