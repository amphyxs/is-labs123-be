package com.example.prac.dto.imports;

import com.example.prac.dto.data.OwnerDTO;

import lombok.Data;

@Data
public class ImportHistoryItemDTO {
    private long id;
    private String status;
    private OwnerDTO owner;
    private Long numberOfAddedObjects;
    private String fileUrl;
}
