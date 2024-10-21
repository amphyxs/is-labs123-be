package com.example.prac.model.dataEntity;

import java.util.Date;

import com.example.prac.model.authEntity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "dragons")
public class Dragon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @NotBlank(message = "Name cannot be null or empty")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Coordinates cannot be null")
    private Coordinates coordinates;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate = new Date();

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cave_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Cave cannot be null")
    private DragonCave cave;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "killer_id", referencedColumnName = "id")
    private Person killer;

    @Min(value = 1, message = "Age must be greater than 0")
    @Column(name = "age")
    private Long age;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DragonType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "character", nullable = false)
    @NotNull(message = "Character cannot be null")
    private DragonCharacter character;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "head_id", referencedColumnName = "id", nullable = true)
    private DragonHead head;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = "Owner cannot be null")
    private User dragonOwner;

    @Column(name = "can_be_edited_by_admin", nullable = false)
    private Boolean canBeEditedByAdmin;
}
