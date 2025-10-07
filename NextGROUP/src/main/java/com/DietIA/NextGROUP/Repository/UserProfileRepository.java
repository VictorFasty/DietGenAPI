package com.DietIA.NextGROUP.Repository;

import com.DietIA.NextGROUP.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByUser_Id(UUID id);
}
