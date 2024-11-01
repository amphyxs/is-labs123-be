package com.example.prac.service.auth;

import com.example.prac.model.auth.AdminRequest;
import com.example.prac.model.auth.Role;
import com.example.prac.model.auth.User;
import com.example.prac.repository.auth.AdminRequestRepository;
import com.example.prac.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepository;
    private final UserRepository userRepository;

    public boolean createAdminRequest(AdminRequest adminRequest) {
        if (adminRequest.getId() == null || getAdminRequestById(adminRequest.getId()).isEmpty()) {
            adminRequestRepository.save(adminRequest);
            return true;
        }
        return false;
    }

    public Optional<AdminRequest> findByRequester(User requester) {
        return adminRequestRepository.findByRequester(requester);
    }

    public Optional<AdminRequest> getAdminRequestById(Long id) {
        return adminRequestRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<AdminRequest> getAllAdminRequests() {
        return (List<AdminRequest>) adminRequestRepository.findAll();
    }

    public boolean approveRequest(Long requestId) {
        Optional<AdminRequest> adminRequestOptional = adminRequestRepository.findById(requestId);

        if (adminRequestOptional.isEmpty())
            return false;

        AdminRequest adminRequest = adminRequestOptional.get();
        adminRequest.setApproved(true);
        adminRequestRepository.save(adminRequest);

        User userToApprove = adminRequest.getRequester();
        userToApprove.setRole(Role.ADMIN);
        userRepository.save(userToApprove);

        return true;
    }
}
