package com.example.prac.model.auth;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admin_request")
@Data
public class AdminRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_requests_seq_generator")
    @SequenceGenerator(name = "admin_requests_seq_generator", sequenceName = "admin_requests_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User requester;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;
}
