package com.example.prac.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.prac.dto.data.DragonDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.data.Dragon;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DragonMapper implements Mapper<Dragon, DragonDTO> {
    private final ModelMapper modelMapper;

    @Override
    public DragonDTO mapTo(Dragon dragon) {
        return modelMapper.map(dragon, DragonDTO.class);
    }

    @Override
    public Dragon mapFrom(DragonDTO dragonDTO) {
        return modelMapper.map(dragonDTO, Dragon.class);
    }
}