package com.example.prac.DTO.data;

import com.example.prac.model.dataEntity.Color;
import lombok.Data;

import java.util.Date;

@Data
public class PersonDTO {
    private long id;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private LocationDTO location;
    private Date birthday;
    private double height;
    private String passportID;
}
