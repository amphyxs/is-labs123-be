package com.example.prac.mappers.impl;

import com.example.prac.DTO.data.PersonDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.dataEntity.Person;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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