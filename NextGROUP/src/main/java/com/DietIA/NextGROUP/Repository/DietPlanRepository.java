package com.DietIA.NextGROUP.Repository;

import com.DietIA.NextGROUP.Model.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, UUID> {
    List<DietPlan> findAllByUser_IdOrderByCreatedAtDesc(UUID userId);
}
