package com.DietIA.NextGROUP.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private Double height;
    private Double weight;
    private LocalDate dateOfBirth;
    private String gender;
    private String activityLevel;
    private String goal;

    @Column(columnDefinition = "TEXT")
    private String dietaryRestrictions;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public UserProfile(User user) {
        this.user = user;
    }
}
