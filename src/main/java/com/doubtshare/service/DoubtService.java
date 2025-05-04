package com.doubtshare.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.doubtshare.dto.CreateDoubtRequest;
import com.doubtshare.dto.DoubtRequestDTO;

public interface DoubtService {
	public DoubtRequestDTO createDoubtRequest(CreateDoubtRequest createDoubtRequest);
	public DoubtRequestDTO completeDoubtRequest(UUID doubtRequestId);
	public DoubtRequestDTO acceptDoubtRequest(UUID doubtRequestId);
	public Page<DoubtRequestDTO> getDoubtHistory(UUID studentId, int page, int size);
	public Page<DoubtRequestDTO> getAllDoubts(int page, int size);
}
