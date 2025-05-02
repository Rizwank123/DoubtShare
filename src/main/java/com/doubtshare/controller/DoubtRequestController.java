package com.doubtshare.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doubtshare.dto.CreateDoubtRequest;
import com.doubtshare.dto.DoubtRequestDTO;
import com.doubtshare.service.DoubtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doubt-request")
public class DoubtRequestController {
	@Autowired
	private DoubtService doubtService;
	
	@PostMapping("/")
	public ResponseEntity<DoubtRequestDTO>  createDoubt(@Valid @RequestBody CreateDoubtRequest request){
		System.out.println("Request: " + request);
		return  ResponseEntity.ok(doubtService.createDoubtRequest(request));
	}
	
	@PutMapping("/{doubtRequestId}/accept")
	@PostAuthorize("hasRole('TUTOR')")
	public ResponseEntity<DoubtRequestDTO> acceptDoubt(@PathVariable UUID doubtRequestId){
		return ResponseEntity.ok(doubtService.acceptDoubtRequest(doubtRequestId));	
	}
	
	@PutMapping("/{doubtRequestId}/complete")
	@PostAuthorize("hasRole('TUTOR')")
	public ResponseEntity<DoubtRequestDTO> completeDoubt(@PathVariable UUID doubtRequestId) {
		return ResponseEntity.ok(doubtService.completeDoubtRequest(doubtRequestId));
	}
	
	@GetMapping("/history/{studentId}")
	public ResponseEntity<Page<DoubtRequestDTO>> getDoubtHistory(@PathVariable UUID studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(doubtService.getDoubtHistory(studentId, page, size));
	
	}
}
