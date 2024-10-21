package com.example.prac.model.dataEntity;

import com.example.prac.model.authEntity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name="locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @NotNull(message = "X cannot be null")
    @Column(name = "x", nullable = false)
    private Integer x;

    @NotNull(message = "Y cannot be null")
    @Column(name = "y", nullable = false)
    private Float y;

    @Size(max = 214, message = "Name length must be no more than 214")
    @Column(name = "name")
    private String name;
}
