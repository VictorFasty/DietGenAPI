package com.DietIA.NextGROUP.Controller;

import com.DietIA.NextGROUP.Controller.DTO.DietPlanDTO.DietPlanResponseDTO;
import com.DietIA.NextGROUP.Controller.Mappers.DietPlanMapper;
import com.DietIA.NextGROUP.Model.DietPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diets")
public class DietPlanController {

    @Autowired
    private com.DietIA.NextGROUP.service.DietPlanService dietPlanService;

    @Autowired
    private DietPlanMapper dietPlanMapper;


    @PostMapping("/generate")
    public ResponseEntity<DietPlanResponseDTO> generateDiet() {

        DietPlan savedDietPlan = dietPlanService.generateAndSaveDietForCurrentUser();


        DietPlanResponseDTO responseDto = dietPlanMapper.toDto(savedDietPlan);


        return ResponseEntity.ok(responseDto);
    }

 
}
