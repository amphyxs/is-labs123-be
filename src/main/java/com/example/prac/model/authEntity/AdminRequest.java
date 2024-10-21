package com.example.prac.model.authEntity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admin_request")
@Data
public class AdminRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;
}
