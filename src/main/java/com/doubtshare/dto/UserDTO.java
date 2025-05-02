package com.doubtshare.dto;

import com.doubtshare.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String username;
    private String email;
    private User.UserType userType;
    private String language;
    private Integer classGrade;
    private Set<String> subjectExpertise;
    private Set<Integer> teachingGrades;
    
    // Don't include password in DTO for security
}