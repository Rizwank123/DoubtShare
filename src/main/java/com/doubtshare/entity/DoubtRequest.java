package com.doubtshare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "doubt_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubtRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor; // Will be null until a tutor accepts the request
    
    @Column(nullable = false)
    private String subject; // The subject of the doubt
    
    @Column(nullable = false, length = 500)
    private String doubtDescription;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoubtStatus status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = DoubtStatus.PENDING;
    }
    
    public enum DoubtStatus {
        PENDING, ACCEPTED, COMPLETED, EXPIRED
    }
}