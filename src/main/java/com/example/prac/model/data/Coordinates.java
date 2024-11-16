package com.example.prac.model.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coordinates_seq_generator")
    @SequenceGenerator(name = "coordinates_seq_generator", sequenceName = "coordinates_seq", allocationSize = 1)
    private long id;

    @Column(name = "x")
    private float x;

    @NotNull(message = "Y cannot be null")
    @Column(name = "y", nullable = false)
    private Long y;
}
