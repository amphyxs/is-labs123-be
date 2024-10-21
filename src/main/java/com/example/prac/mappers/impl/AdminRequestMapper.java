package com.example.prac.mappers.impl;

import com.example.prac.DTO.admin.AdminRequestDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.authEntity.AdminRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AdminRequestMapper implements Mapper<AdminRequest, AdminRequestDTO> {
    private final ModelMapper modelMapper;

    @Override
    public AdminRequestDTO mapTo(AdminRequest AdminRequest) {
        return modelMapper.map(AdminRequest, AdminRequestDTO.class);
    }

    @Override
    public AdminRequest mapFrom(AdminRequestDTO AdminRequestDTO) {
        return modelMapper.map(AdminRequestDTO, AdminRequest.class);
    }

}