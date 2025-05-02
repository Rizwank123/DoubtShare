package com.doubtshare.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.doubtshare.dto.CreateDoubtRequest;
import com.doubtshare.dto.DoubtRequestDTO;
import com.doubtshare.entity.User;

public interface DoubtService {
	public DoubtRequestDTO createDoubtRequest(CreateDoubtRequest createDoubtRequest);
	public DoubtRequestDTO completeDoubtRequest(UUID doubtRequestId);
	public DoubtRequestDTO acceptDoubtRequest(UUID doubtRequestId);
	public Page<DoubtRequestDTO> getDoubtHistory(UUID studentId, int page, int size);
}
