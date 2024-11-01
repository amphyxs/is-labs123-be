package com.example.prac.dto.data;

import lombok.Data;

import java.util.Date;

import com.example.prac.model.data.Color;
import com.example.prac.model.data.DragonCharacter;
import com.example.prac.model.data.DragonType;

@Data
public class DragonDTO {
    private long id;
    private String name;
    private CoordinatesDTO coordinates;
    private Date creationDate;
    private DragonCaveDTO cave;
    private PersonDTO killer;
    private Long age;
    private Color color;
    private DragonType type;
    private DragonCharacter character;
    private DragonHeadDTO head;
    private OwnerDTO owner;
    private Boolean canBeEditedByAdmin;
}
