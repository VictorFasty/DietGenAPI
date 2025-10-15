package com.DietIA.NextGROUP.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "diet_plans")
@Getter
@Setter
@NoArgsConstructor
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDate createdAt; // Data em que o plano foi gerado

    // --- RELACIONAMENTOS ---

    // Muitos planos de dieta podem pertencer a UM usuário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // UM plano de dieta tem MUITAS refeições.
    // CascadeType.ALL: Se salvar/apagar o plano, salve/apague as refeições junto.
    // orphanRemoval = true: Se remover uma refeição da lista, apague-a do banco.
    @OneToMany(mappedBy = "dietPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meal> meals = new ArrayList<>();

    // --- MÉTODOS AUXILIARES (BOA PRÁTICA) ---
    public void addMeal(Meal meal) {
        meals.add(meal);
        meal.setDietPlan(this); // Garante a consistência do relacionamento
    }
}

