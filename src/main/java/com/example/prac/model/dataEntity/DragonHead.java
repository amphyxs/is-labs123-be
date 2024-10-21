package com.example.prac.model.dataEntity;

import com.example.prac.model.authEntity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name="dragon_heads")
public class DragonHead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Min(value = 1, message = "Size must be greater than 0")
    @Column(name = "size", nullable = false)
    private long size;
}