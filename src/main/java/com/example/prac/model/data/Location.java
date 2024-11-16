package com.example.prac.model.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locations_seq_generator")
    @SequenceGenerator(name = "locations_seq_generator", sequenceName = "locations_seq", allocationSize = 1)
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
