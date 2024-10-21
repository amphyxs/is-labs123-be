package com.example.prac.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prac.DTO.admin.AdminRequestDTO;
import com.example.prac.mappers.impl.AdminRequestMapper;
import com.example.prac.model.authEntity.AdminRequest;
import com.example.prac.model.authEntity.User;
import com.example.prac.service.auth.AdminRequestService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin-request")
public class AdminRequestController {

    private final AdminRequestService adminRequestService;
    private final AdminRequestMapper adminRequestMapper;

    @PostMapping("/request")
    public ResponseEntity<String> requestAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        AdminRequest adminRequest = new AdminRequest();
        adminRequest.setRequester(currentUser);
        adminRequest.setApproved(false);
        if (adminRequestService.createAdminRequest(adminRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Запрос на админку создан.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Запрос уже был отправлен.");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> getAdminRequestStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Optional<AdminRequest> existingRequest = adminRequestService.findByRequester(currentUser);

        if (existingRequest.isPresent()) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdminRequestDTO>> getAllRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
            List<AdminRequest> requests = adminRequestService.getAllAdminRequests();
            List<AdminRequestDTO> requestDTOs = requests.stream()
                    .map(adminRequestMapper::mapTo)
                    .toList();
            return ResponseEntity.ok(requestDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {

            boolean isApproved = adminRequestService.approveRequest(id);
            if (isApproved) {
                return ResponseEntity.ok("Запрос успешно одобрен.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Запрос уже одобрен этим админом или не найден.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminRequest> getRequestById(@PathVariable Long id) {
        return adminRequestService.getAdminRequestById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
