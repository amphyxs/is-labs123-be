package com.example.prac.dto.admin;

import lombok.Data;

@Data
public class AdminRequestDTO {
    private Long id;
    private String requesterUsername;
    private boolean isApproved;
}
