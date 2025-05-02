package com.doubtshare.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doubtshare.dto.LoggerDto;
import com.doubtshare.entity.LoggerEntity;
import com.doubtshare.repository.LoggerRepository;
import com.doubtshare.service.LoggerService;

@RestController
@RequestMapping("/api/logger")
public class LoggerController {
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private LoggerRepository loggerRepository;
	 @GetMapping("/logs")
	 public List<LoggerDto> getAllLogs() {
	 return loggerService.getAllLogs();
	 }
	

	    @GetMapping("/test-mongo")
	    public String testMongo() {
	        LoggerEntity log = LoggerEntity.builder()
	                .action("Direct call")
	                .timestamp(LocalDateTime.now().toString())
	                .details("Called /test-mongo endpoint")
	                .status("Success")
	                .build();

	        loggerRepository.save(log);
	        return "Saved log to Mongo!";
	    }

}
