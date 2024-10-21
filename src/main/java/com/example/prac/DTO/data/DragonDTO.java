package com.example.prac.DTO.data;

import com.example.prac.model.dataEntity.Color;
import com.example.prac.model.dataEntity.DragonCharacter;
import com.example.prac.model.dataEntity.DragonType;
import lombok.Data;

import java.util.Date;

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
