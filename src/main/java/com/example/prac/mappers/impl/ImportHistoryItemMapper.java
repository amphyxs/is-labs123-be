package com.example.prac.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.prac.dto.imports.ImportHistoryItemDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.imports.ImportHistoryItem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImportHistoryItemMapper implements Mapper<ImportHistoryItem, ImportHistoryItemDTO> {
    private final ModelMapper modelMapper;

    @Override
    public ImportHistoryItemDTO mapTo(ImportHistoryItem item) {
        return modelMapper.map(item, ImportHistoryItemDTO.class);
    }

    @Override
    public ImportHistoryItem mapFrom(ImportHistoryItemDTO itemDTO) {
        return modelMapper.map(itemDTO, ImportHistoryItem.class);
    }
}
