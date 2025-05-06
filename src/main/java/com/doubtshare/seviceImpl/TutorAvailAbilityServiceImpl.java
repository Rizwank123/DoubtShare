package com.doubtshare.seviceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.doubtshare.dto.UserDTO;
import com.doubtshare.entity.LoggerEntity;
import com.doubtshare.entity.TutorAvailability;
import com.doubtshare.entity.User;
import com.doubtshare.entity.User.UserType;
import com.doubtshare.repository.LoggerRepository;
import com.doubtshare.repository.TutorAvailabilityRepository;
import com.doubtshare.repository.UserRepository;
import com.doubtshare.service.TutorAvailabilityService;

import jakarta.transaction.Transactional;

@Service
public class TutorAvailAbilityServiceImpl implements TutorAvailabilityService {

		@Autowired
	    private TutorAvailabilityRepository tutorAvailabilityRepository;
		@Autowired
		private LoggerRepository loggerRepository;
	    @Autowired
	    private UserRepository userRepository;
	    // Variable to store the current count of online tutors
	    private long onlineTutorCount = 0;
	    
	    @Transactional
	    public void updateTutorPingTime(UUID tutorId) {
	        TutorAvailability tutorAvailability = tutorAvailabilityRepository.findByTutorId(tutorId)
	                .orElseThrow(() -> new RuntimeException("Tutor availability not found"));
	                
	        
			User tutor = userRepository.findById(tutorId).orElseThrow(() -> new RuntimeException("Tutor not found"));	
			if (tutor == null) {
				throw new RuntimeException("Tutor not found");
			}
			if (tutor.getUserType() != UserType.TUTOR) {
                throw new RuntimeException("Only tutors can ping to update availability");
            }
            
            tutorAvailability.setLastPingTime(LocalDateTime.now().plusMinutes(3));
            tutorAvailabilityRepository.save(tutorAvailability);
            LoggerEntity loggerEntity = LoggerEntity.builder()
                    .action("Ping")
                    .timestamp(LocalDateTime.now().toString())
                    .details("Tutor pinged to update availability")
                    .status("Success")
                    .errorMessage(null)
                    .build();
                    loggerRepository.save(loggerEntity);
        
		}

		@Transactional
		public void updateTutorPingTime(UserDTO tutor) {
			TutorAvailability tutorAvailability = tutorAvailabilityRepository.findById(tutor.getId())
					.orElseThrow(() -> new RuntimeException("Tutor availability not found"));
			if (tutor.getUserType() != UserType.TUTOR){
				throw new RuntimeException("Only tutors can ping to update availability");
			}
			
	        tutorAvailability.setLastPingTime(LocalDateTime.now());
	        tutorAvailabilityRepository.save(tutorAvailability);
	        LoggerEntity loggerEntity = LoggerEntity.builder()
					.action("Ping")
					.timestamp(LocalDateTime.now().toString())
					.details("Tutor pinged to update availability")
					.status("Success")
					.errorMessage(null)
					.build();
	                loggerRepository.save(loggerEntity);
	        
	    }
	    
		@Transactional
		public void setAvailabilityStatus(UserDTO tutorDTO, boolean isAvailable) {
		    if (tutorDTO.getUserType() != UserType.TUTOR) {
		        throw new RuntimeException("Only tutors can set availability status");
		    }

		    // Fetch the User entity from DB — this is required due to @OneToOne @MapsId
		    User tutor = userRepository.findById(tutorDTO.getId())
		            .orElseThrow(() -> new RuntimeException("Tutor not found"));

		    TutorAvailability tutorAvailability = tutorAvailabilityRepository
		            .findByTutorId(tutorDTO.getId())
		            .orElse(null);

		    if (tutorAvailability == null && isAvailable) {
		        tutorAvailability = new TutorAvailability();
		        tutorAvailability.setTutor(tutor); // Set entity, not just ID
		        tutorAvailability.setAvailable(true);
		        tutorAvailability.setLastPingTime(LocalDateTime.now());
		    } else if (tutorAvailability != null && isAvailable) {
		        tutorAvailability.setLastPingTime(LocalDateTime.now());
		    }

		    if (tutorAvailability != null) {
		        tutorAvailability.setTutor(tutor); // always set it
		        tutorAvailability.setAvailable(isAvailable);
		        tutorAvailabilityRepository.save(tutorAvailability);
		    }

		    LoggerEntity loggerEntity = LoggerEntity.builder()
		        .action("Set Availability")
		        .timestamp(LocalDateTime.now().toString())
		        .details("Tutor set availability status to " + isAvailable)
		        .status("Success")
		        .errorMessage(null)
		        .build();

		    loggerRepository.save(loggerEntity);
		}

	    // CRON job that runs every second to count online tutors
	    @Scheduled(fixedRate = 10000) // 1000 ms = 1 second
	    public void countAndUpdateOnlineTutors() {
	        LocalDateTime cutoffTime = LocalDateTime.now().minusSeconds(3);
	        onlineTutorCount = tutorAvailabilityRepository.countOnlineTutors(cutoffTime);
	        
	    }
	    
	    @Override
	    public long countOnlineTutors() {
	        return onlineTutorCount;
	    }
	    
	    // Helper method to get a list of tutors who are online and available
	    public boolean isTutorOnlineAndAvailable(UUID tutorId) {
	        TutorAvailability tutorAvailability = tutorAvailabilityRepository.findById(tutorId).orElse(null);
	        if (tutorAvailability == null) {
	            return false;
	        }
	        
	        LocalDateTime cutoffTime = LocalDateTime.now().minusSeconds(3);
	        return tutorAvailability.isAvailable() && tutorAvailability.getLastPingTime().isAfter(cutoffTime);
	    }

		
}
