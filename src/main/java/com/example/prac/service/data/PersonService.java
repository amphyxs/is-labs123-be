package com.example.prac.service.data;

import com.example.prac.DTO.data.PersonDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.dataEntity.Person;
import com.example.prac.repository.data.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final Mapper<Person, PersonDTO> personMapper;

    public PersonDTO save(PersonDTO personDTO) {
        Person person = personMapper.mapFrom(personDTO);
        return personMapper.mapTo(personRepository.save(person));
    }

    public List<PersonDTO> findAllPersons() {
        return StreamSupport.stream(personRepository.findAll().spliterator(), false)
                .map(personMapper::mapTo).toList();
    }

    public Optional<PersonDTO> findById(Long personId) {
        Optional<Person> optionalPerson = personRepository.findById(personId);
        return optionalPerson.map(personMapper::mapTo);
    }

    public boolean isExists(Long personId) {
        return personRepository.existsById(personId);
    }

    public PersonDTO partialUpdate(Long personId, PersonDTO personDTO) {
        personDTO.setId(personId);
        return personRepository.findById(personId).map(existingPerson -> {
            PersonDTO existingPersonDto = personMapper.mapTo(existingPerson);
            Optional.ofNullable(personDTO.getName()).ifPresent(existingPersonDto::setName);
            Optional.ofNullable(personDTO.getEyeColor()).ifPresent(existingPersonDto::setEyeColor);
            Optional.ofNullable(personDTO.getHairColor()).ifPresent(existingPersonDto::setHairColor);
            Optional.ofNullable(personDTO.getLocation()).ifPresent(existingPersonDto::setLocation);
            Optional.ofNullable(personDTO.getBirthday()).ifPresent(existingPersonDto::setBirthday);
            Optional.of(personDTO.getHeight()).ifPresent(existingPersonDto::setHeight);
            Optional.ofNullable(personDTO.getPassportID()).ifPresent(existingPersonDto::setPassportID);
            return personMapper.mapTo(personRepository.save(personMapper.mapFrom(existingPersonDto)));
        }).orElseThrow(() -> new RuntimeException("Person doesn't exist"));
    }

    public void delete(Long personId) {
        personRepository.deleteById(personId);
    }
}