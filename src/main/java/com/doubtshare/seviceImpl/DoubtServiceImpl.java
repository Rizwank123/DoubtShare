package com.doubtshare.seviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.doubtshare.dto.CreateDoubtRequest;
import com.doubtshare.dto.DoubtRequestDTO;
import com.doubtshare.entity.DoubtRequest;
import com.doubtshare.entity.LoggerEntity;
import com.doubtshare.entity.TutorAvailability;
import com.doubtshare.entity.User;
import com.doubtshare.repository.DoubtRequestRepository;
import com.doubtshare.repository.LoggerRepository;
import com.doubtshare.repository.TutorAvailabilityRepository;
import com.doubtshare.repository.UserRepository;
import com.doubtshare.security.UserDetailsImpl;
import com.doubtshare.service.DoubtService;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class DoubtServiceImpl implements DoubtService {
	@Autowired
    private DoubtRequestRepository doubtRequestRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TutorAvailabilityRepository tutorAvailabilityRepository;
    @Autowired
    private LoggerRepository loggerRepository;
    @Transactional
	@Override
	public DoubtRequestDTO createDoubtRequest(CreateDoubtRequest createDoubtRequest) {
    	  UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext()
                 .getAuthentication().getPrincipal();
			User student = userRepository.findById(user.getId())
					.orElseThrow(() -> new RuntimeException("Student not found"));

			// Check if the student is eligible to create a doubt request
			if (student.getUserType() != User.UserType.STUDENT) {
				throw new RuntimeException("Only students can create doubt requests");
			}

			
    	  // Create the doubt request
        DoubtRequest doubtRequest = new DoubtRequest();
        doubtRequest.setStudent(student);
        doubtRequest.setSubject(createDoubtRequest.getSubject());
        doubtRequest.setDoubtDescription(createDoubtRequest.getDoubtDescription());
        doubtRequest.setStatus(DoubtRequest.DoubtStatus.PENDING);
        
        // Save the doubt request
        DoubtRequest savedRequest = doubtRequestRepository.save(doubtRequest);
        
        // Log the creation of the doubt request
		LoggerEntity loggerEntity = LoggerEntity.builder()
				.action("Create Doubt Request")
				.timestamp(LocalDateTime.now().toString())
				.details("Doubt request created with ID: " + savedRequest.getId())
				.status("Success").errorMessage(null)
				.build();
		loggerRepository.save(loggerEntity);
        // Find eligible tutors for this doubt
        List<User> eligibleTutors = userRepository.findEligibleTutors(
                student.getLanguage(),
                createDoubtRequest.getSubject()
        );

        
        // Get the IDs of eligible tutors
        List<UUID> eligibleTutorIds = eligibleTutors.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        
        // Find which of these tutors are online
        LocalDateTime cutoffTime = LocalDateTime.now().minusSeconds(3);
        List<TutorAvailability> onlineTutors = tutorAvailabilityRepository.findByTutorIdIn(eligibleTutorIds)
                .stream()
                .filter(ta -> ta.getLastPingTime().isAfter(cutoffTime) && ta.isAvailable())
                .collect(Collectors.toList());
        
        // In a real app, we would notify these online tutors about the doubt request
        // For this implementation, we'll just check if any tutors are available
        if (onlineTutors.isEmpty()) {
            // No tutors available - update status to EXPIRED
            savedRequest.setStatus(DoubtRequest.DoubtStatus.PENDING);
            doubtRequestRepository.save(savedRequest);
        }
        
        return convertToDTO(savedRequest);
	}

    @Transactional
	@Override
	public DoubtRequestDTO completeDoubtRequest(UUID doubtRequestId) {
		DoubtRequest doubtRequest = doubtRequestRepository.findById(doubtRequestId)
                .orElseThrow(() -> new RuntimeException("Doubt request not found"));
		log.info("Doubt request found: " + doubtRequest);
		// Get user from contextHolder
		UserDetailsImpl user =  (UserDetailsImpl) SecurityContextHolder.getContext()
	                .getAuthentication().getPrincipal();
		User tutor = userRepository.findById(user.getId())
				                .orElseThrow(() -> new RuntimeException("Tutor not found"));
		log.info("Tutor found: " + tutor);

        // Check if this tutor is assigned to this doubt
        if (!doubtRequest.getTutor().getId().equals(tutor.getId())) {
            throw new RuntimeException("You are not assigned to this doubt request");
        }
        
        // Check if the request is accepted
        if (doubtRequest.getStatus() != DoubtRequest.DoubtStatus.ACCEPTED) {
            throw new RuntimeException("This doubt request cannot be completed");
        }
        
        // Update status
        doubtRequest.setStatus(DoubtRequest.DoubtStatus.COMPLETED);
        doubtRequest.setCompletedAt(LocalDateTime.now());
        
        DoubtRequest updatedRequest = doubtRequestRepository.save(doubtRequest);
        
        return convertToDTO(updatedRequest);	
        
    }
    @Transactional
	@Override
	public DoubtRequestDTO acceptDoubtRequest(UUID doubtRequestId) {
    	log.info("Accepting doubt request with ID: " + doubtRequestId);
		DoubtRequest doubtRequest = doubtRequestRepository.findById(doubtRequestId)
                .orElseThrow(() -> new RuntimeException("Doubt request not found"));
		
		
		
		// Get Tutor From ContextHolder
		UserDetailsImpl user =  (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
		User tutor = userRepository.findById(user.getId())
				                .orElseThrow(() -> new RuntimeException("Tutor not found"));
		
        
        // Check if the request is still pending
        if (doubtRequest.getStatus() != DoubtRequest.DoubtStatus.PENDING) {
            throw new RuntimeException("This doubt request is no longer available");
        }
        
        // Check if tutor is eligible
        if (!tutor.getUserType().equals(User.UserType.TUTOR)) {
            throw new RuntimeException("Only tutors can accept doubt requests");
        }
        
        // Check if tutor has expertise in this subject
        if (!tutor.getSubjectExpertise().contains(doubtRequest.getSubject())) {
            throw new RuntimeException("You don't have expertise in this subject");
        }
        
        // Check if tutor can teach this grade
        if (!tutor.getTeachingGrades().contains(doubtRequest.getStudent().getClassGrade())) {
            throw new RuntimeException("You don't teach this grade");
        }
        
        // Check if languages match
        if (!tutor.getLanguage().equals(doubtRequest.getStudent().getLanguage())) {
            throw new RuntimeException("Language mismatch");
        }
        
        // Assign the tutor and update status
        doubtRequest.setTutor(tutor);
        doubtRequest.setStatus(DoubtRequest.DoubtStatus.ACCEPTED);
        doubtRequest.setAcceptedAt(LocalDateTime.now());
        
        DoubtRequest updatedRequest = doubtRequestRepository.save(doubtRequest);
        LoggerEntity loggerEntity = LoggerEntity.builder()
        		                .action("Accept Doubt Request")
        		                .timestamp(LocalDateTime.now().toString())
        		                .details("Tutor accepted doubt request with ID: " + doubtRequestId)
        		                .status("Success")
        		                .errorMessage(null)
        		                .build();
        
        loggerRepository.save(loggerEntity);
        return convertToDTO(updatedRequest);
	}

	@Override
	public Page<DoubtRequestDTO> getDoubtHistory(UUID studentId, int page, int size) {
		// Create a pageable with sorting by created time (descending)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        // Find all the student's doubts
        Page<DoubtRequest> doubtRequests = doubtRequestRepository.findByStudentIdOrderByCreatedAtDesc(studentId, pageable);
        
        // Convert each doubt request to DTO
        return doubtRequests.map(this::convertToDTO);
	}
	
	
	public DoubtRequestDTO convertToDTO(DoubtRequest doubtRequest) {
        DoubtRequestDTO dto = new DoubtRequestDTO();
        dto.setId(doubtRequest.getId());
        dto.setStudentId(doubtRequest.getStudent().getId());
        dto.setStudentUsername(doubtRequest.getStudent().getUsername());
        
        if (doubtRequest.getTutor() != null) {
            dto.setTutorId(doubtRequest.getTutor().getId());
            dto.setTutorUsername(doubtRequest.getTutor().getUsername());
        }
        
        dto.setSubject(doubtRequest.getSubject());
        dto.setDoubtDescription(doubtRequest.getDoubtDescription());
        dto.setStatus(doubtRequest.getStatus());
        dto.setCreatedAt(doubtRequest.getCreatedAt());
        dto.setAcceptedAt(doubtRequest.getAcceptedAt());
        dto.setCompletedAt(doubtRequest.getCompletedAt());
        
        return dto;
	}

	@Override
	public Page<DoubtRequestDTO> getAllDoubts(int page, int size) {
		// Create a pageable with sorting by created time (descending)
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

		// Find all the doubts
		Page<DoubtRequest> doubtRequests = doubtRequestRepository.findAll(pageable);

		// Convert each doubt request to DTO
		Page<DoubtRequestDTO> dtoPage = doubtRequests.map(this::convertToDTO);

		return dtoPage;
	}

}
