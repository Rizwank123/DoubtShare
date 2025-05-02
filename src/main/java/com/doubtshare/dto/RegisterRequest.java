package com.doubtshare.dto;

import com.doubtshare.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private User.UserType userType;
    private String language;
    private Integer classGrade; // For students
    private Set<String> subjectExpertise; // For tutors
    private Set<Integer> teachingGrades; // For tutors
}