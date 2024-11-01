package com.example.prac.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.prac.dto.data.PersonDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.data.Person;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor

public class PersonMapper implements Mapper<Person, PersonDTO> {
    private final ModelMapper modelMapper;

    public PersonMapper() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public PersonDTO mapTo(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    @Override
    public Person mapFrom(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
