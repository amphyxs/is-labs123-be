package com.example.prac.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.prac.dto.admin.AdminRequestDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.auth.AdminRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminRequestMapper implements Mapper<AdminRequest, AdminRequestDTO> {
    private final ModelMapper modelMapper;

    @Override
    public AdminRequestDTO mapTo(AdminRequest adminRequest) {
        return modelMapper.map(adminRequest, AdminRequestDTO.class);
    }

    @Override
    public AdminRequest mapFrom(AdminRequestDTO adminRequestDTO) {
        return modelMapper.map(adminRequestDTO, AdminRequest.class);
    }

}