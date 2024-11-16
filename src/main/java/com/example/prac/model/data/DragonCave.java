package com.example.prac.model.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
@Table(name = "dragon_caves")
public class DragonCave {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dragon_caves_seq_generator")
    @SequenceGenerator(name = "dragon_caves_seq_generator", sequenceName = "dragon_caves_seq", allocationSize = 1)
    private long id;

    @Min(value = 1, message = "Number of treasures must be greater than 0")
    @Column(name = "number_of_treasures")
    private Long numberOfTreasures;
}
