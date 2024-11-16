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
@Table(name = "dragon_heads")
public class DragonHead {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dragon_heads_seq_generator")
    @SequenceGenerator(name = "dragon_heads_seq_generator", sequenceName = "dragon_heads_seq", allocationSize = 1)
    private long id;

    @Min(value = 1, message = "Size must be greater than 0")
    @Column(name = "size", nullable = false)
    private long size;
}