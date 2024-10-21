package com.example.prac.model.dataEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
@Table(name="dragon_caves")
public class DragonCave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Min(value = 1, message = "Number of treasures must be greater than 0")
    @Column(name = "number_of_treasures")
    private Long numberOfTreasures;
}
