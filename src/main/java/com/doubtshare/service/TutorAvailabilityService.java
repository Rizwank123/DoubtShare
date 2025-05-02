package com.doubtshare.service;

import java.util.UUID;

import com.doubtshare.dto.UserDTO;

public interface TutorAvailabilityService {
	public void updateTutorPingTime(UUID tutorId); 
	public void setAvailabilityStatus(UserDTO userDto, boolean isAvailable);
	public boolean isTutorOnlineAndAvailable(UUID tutorId);
	public void countAndUpdateOnlineTutors();
	public long countOnlineTutors();

}
