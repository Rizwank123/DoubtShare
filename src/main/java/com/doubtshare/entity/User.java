package com.doubtshare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType; // STUDENT or TUTOR
    
    @Column(nullable = false)
    private String language; // The language the user communicates in
    
    @Column(name = "class_grade")
    private Integer classGrade; // For students - their grade level
    
    @ElementCollection
    @CollectionTable(name = "tutor_subject_expertise", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "subject")
    @Builder.Default
    private Set<String> subjectExpertise = new HashSet<>(); // For tutors - subjects they can teach
    
    @ElementCollection
    @CollectionTable(name = "tutor_grades", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "grade")
    @Builder.Default
    private Set<Integer> teachingGrades = new HashSet<>(); // For tutors - grades they can teach
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum UserType {
        STUDENT, TUTOR
    }
}