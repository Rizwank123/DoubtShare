package com.doubtshare.repository;

import com.doubtshare.entity.DoubtRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoubtRequestRepository extends JpaRepository<DoubtRequest, UUID> {
    
    // Find all doubts by student ID, ordered by created time (for doubt history)
    Page<DoubtRequest> findByStudentIdOrderByCreatedAtDesc(UUID studentId, Pageable pageable);
    
    // Find pending doubts for a specific tutor
    List<DoubtRequest> findByStatusAndTutorId(DoubtRequest.DoubtStatus status, UUID tutorId);
    
    // Find pending doubts with no assigned tutor
    List<DoubtRequest> findByStatusAndTutorIsNull(DoubtRequest.DoubtStatus status);
}