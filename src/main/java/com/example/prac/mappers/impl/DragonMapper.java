package com.example.prac.mappers.impl;


import com.example.prac.DTO.data.DragonDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.dataEntity.Dragon;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DragonMapper implements Mapper<Dragon, DragonDTO> {
    private final ModelMapper modelMapper;

    @Override
    public DragonDTO mapTo(Dragon Dragon) {
        return modelMapper.map(Dragon, DragonDTO.class);
    }

    @Override
    public Dragon mapFrom(DragonDTO DragonDTO) {
        return modelMapper.map(DragonDTO, Dragon.class);
    }
}