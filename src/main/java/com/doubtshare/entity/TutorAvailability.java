package com.doubtshare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tutor_availability")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TutorAvailability {
    
    @Id
    private UUID tutorId; // Using the tutor's user ID as primary key
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "tutor_id")
    private User tutor;
    
    @Column(name = "last_ping_time")
    private LocalDateTime lastPingTime;
    
    @Column(name = "is_available")
    private boolean isAvailable; // If tutor is currently available to take doubts
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}