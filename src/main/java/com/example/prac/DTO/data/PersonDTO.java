package com.example.prac.dto.data;

import lombok.Data;

import java.util.Date;

import com.example.prac.model.data.Color;

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
