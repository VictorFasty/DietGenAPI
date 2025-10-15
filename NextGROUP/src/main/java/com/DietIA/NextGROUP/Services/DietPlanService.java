package com.DietIA.NextGROUP.service; // Usei 'service' minúsculo como convenção

import com.DietIA.NextGROUP.Model.*;
import com.DietIA.NextGROUP.Repository.DietPlanRepository;
import com.DietIA.NextGROUP.Repository.UserProfileRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class DietPlanService {

    @Autowired
    private DietPlanRepository dietPlanRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private com.DietIA.NextGROUP.service.OpenAIService openAIService;
    @Autowired
    private ObjectMapper objectMapper;


    @Transactional
    public DietPlan generateAndSaveDietForCurrentUser() {
        User currentUser = getCurrentUser();
        UserProfile userProfile = userProfileRepository.findByUser_Id(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("Perfil do utilizador não encontrado. Por favor, preencha o seu perfil primeiro."));

        String prompt = buildPromptForUser(currentUser, userProfile);
        String openAIResponseJson = openAIService.getDietFromOpenAI(prompt);
        DietPlan dietPlan = parseOpenAIResponseAndCreateDietPlan(openAIResponseJson, currentUser);

        return dietPlanRepository.save(dietPlan);
    }


    //prompt que vai ser enviado pro chatgpt
    private String buildPromptForUser(User user, UserProfile profile) {
        int age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
        return String.format(
                """
                Aja como um nutricionista profissional. Crie um plano alimentar detalhado para um dia, em formato JSON, para uma pessoa com as seguintes características:
                - Nome: %s, Idade: %d anos, Altura: %.1f cm, Peso: %.1f kg, Sexo: %s
                - Nível de Atividade: %s, Objetivo: %s, Restrições Alimentares: %s

                O plano deve conter 5 refeições: Café da Manhã, Lanche da Manhã, Almoço, Lanche da Tarde e Jantar.
                Para cada refeição, liste os alimentos, a quantidade em gramas, calorias e os macros (proteínas, carboidratos, gorduras).
                O JSON de resposta deve seguir ESTRITAMENTE este formato (não inclua nenhuma explicação ou texto fora do JSON):
                {
                  "meals": [
                    {
                      "meal_name": "Café da Manhã",
                      "items": [
                        { "food_name": "Ovo Cozido", "quantity_grams": 100, "calories": 155, "protein": 13, "carbs": 1.1, "fat": 11 }
                      ]
                    }
                  ]
                }
                """,
                user.getName(), age, profile.getHeight(), profile.getWeight(), profile.getGender(),
                profile.getActivityLevel(), profile.getGoal(), profile.getDietaryRestrictions()
        );
    } // << --- A CHAVE FOI FECHADA AQUI, NO LUGAR CORRETO

    // AGORA OS OUTROS MÉTODOS ESTÃO NO NÍVEL DA CLASSE, CORRETAMENTE
    private DietPlan parseOpenAIResponseAndCreateDietPlan(String jsonResponse, User user) {
        try {
            OpenAIDietResponse responseDTO = objectMapper.readValue(jsonResponse, OpenAIDietResponse.class);

            DietPlan dietPlan = new DietPlan();
            dietPlan.setUser(user);
            dietPlan.setCreatedAt(LocalDate.now());

            for (MealResponse mealDTO : responseDTO.meals()) {
                Meal meal = new Meal();
                meal.setName(mealDTO.mealName());

                for (FoodItemResponse itemDTO : mealDTO.items()) {
                    FoodItem foodItem = new FoodItem();
                    foodItem.setFoodName(itemDTO.foodName());
                    foodItem.setQuantityGrams(itemDTO.quantityGrams());
                    foodItem.setCalories(itemDTO.calories());
                    foodItem.setProtein(itemDTO.protein());
                    foodItem.setCarbs(itemDTO.carbs());
                    foodItem.setFat(itemDTO.fat());
                    meal.addFoodItem(foodItem);
                }
                dietPlan.addMeal(meal);
            }
            return dietPlan;
        } catch (JsonProcessingException e) {
            System.err.println("JSON recebido da OpenAI: " + jsonResponse);
            throw new RuntimeException("Falha ao fazer o parsing da resposta da OpenAI.", e);
        }
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private record OpenAIDietResponse(List<MealResponse> meals) {}
    private record MealResponse(@JsonProperty("meal_name") String mealName, List<FoodItemResponse> items) {}
    private record FoodItemResponse(
            @JsonProperty("food_name") String foodName,
            @JsonProperty("quantity_grams") Double quantityGrams,
            Double calories,
            Double protein,
            Double carbs,
            Double fat
    ) {}
}