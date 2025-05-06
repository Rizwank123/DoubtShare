package com.doubtshare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doubtshare.dto.UserDTO;
import com.doubtshare.entity.User;
import com.doubtshare.service.TutorAvailabilityService;
import com.doubtshare.service.UserService;

@RestController
@RequestMapping("/api/tutor-availability")

public class TutorAvailabilityController {
    
    @Autowired
    private TutorAvailabilityService tutorAvailabilityService;
    
    @Autowired
    private UserService userService;
    
    // End point for tutor to update their ping time (called by polling function)
    @PostMapping("/ping")
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<Void> updatePingTime(
            @RequestHeader("Authorization") String token) {
        
        // Extract token
        String actualToken = token.replace("Bearer ", "");
        
        // Get the current user
        User tutor = userService.getUserByToken(actualToken);
        
        
        // Update tutor's ping time
        tutorAvailabilityService.updateTutorPingTime(tutor.getId());
        
        return ResponseEntity.ok().build();
    }
    
    // End point to set tutor availability status
    @PostMapping("/status")
    public ResponseEntity<Void> setAvailabilityStatus(
            @RequestHeader("Authorization") String token,
            @RequestParam boolean isAvailable) {
        
        // Extract token
        String actualToken = token.replace("Bearer ", "");
        
        // Get the current user
        User tutor = userService.getUserByToken(actualToken);
        
		UserDTO user = UserDTO.builder()
				.id(tutor.getId())
				.email(tutor.getEmail())
				.userType(tutor.getUserType())
				.username(tutor.getUsername())
				.fullName(tutor.getFullName())
				.language(tutor.getLanguage())
				.classGrade(tutor.getClassGrade())
				.subjectExpertise(tutor.getSubjectExpertise())
				.teachingGrades(tutor.getTeachingGrades())
				.build();
        // Update tutor's availability status
        tutorAvailabilityService.setAvailabilityStatus(user, isAvailable);
        
        return ResponseEntity.ok().build();
    }
    
    // End point to get online tutor count (for display purposes)
    @GetMapping("/online-count")
    public ResponseEntity<Long> getOnlineTutorCount() {
        long count = tutorAvailabilityService.countOnlineTutors();
        return ResponseEntity.ok(count);
    }
}