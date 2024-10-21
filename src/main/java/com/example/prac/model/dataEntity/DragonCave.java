package com.example.prac.model.dataEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
@Table(name = "dragon_caves")
public class DragonCave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Min(value = 1, message = "Number of treasures must be greater than 0")
    @Column(name = "number_of_treasures")
    private Long numberOfTreasures;
}
