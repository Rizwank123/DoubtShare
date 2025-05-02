package com.doubtshare.repository;

import com.doubtshare.entity.TutorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TutorAvailabilityRepository extends JpaRepository<TutorAvailability, UUID> {
    
    @Query("SELECT ta FROM TutorAvailability ta WHERE ta.lastPingTime >= :cutoffTime AND ta.isAvailable = true")
    List<TutorAvailability> findOnlineTutors(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    @Query("SELECT COUNT(ta) FROM TutorAvailability ta WHERE ta.lastPingTime >= :cutoffTime AND ta.isAvailable = true")
    long countOnlineTutors(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    List<TutorAvailability> findByTutorIdIn(List<UUID> tutorIds);
}