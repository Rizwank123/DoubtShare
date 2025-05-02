package com.doubtshare.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doubtshare.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	 Optional<User> findByUsername(String username);
	    
	    boolean existsByUsername(String username);
	    
	    boolean existsByEmail(String email);
	    
	    @Query("SELECT u FROM User u WHERE u.userType = 'TUTOR' " +
	    	       "AND u.language = :language " +
	    	       "AND :subject MEMBER OF u.subjectExpertise")
	    	List<User> findEligibleTutors(
	    	        @Param("language") String language,
	    	        @Param("subject") String subject);


}
