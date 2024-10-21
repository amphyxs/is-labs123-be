package com.example.prac.model.dataEntity;

import com.example.prac.model.authEntity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name="coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "x")
    private float x;

    @NotNull(message = "Y cannot be null")
    @Column(name = "y", nullable = false)
    private Long y;
}
