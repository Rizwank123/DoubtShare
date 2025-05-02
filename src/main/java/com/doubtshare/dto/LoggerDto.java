package com.doubtshare.dto;

import lombok.Data;

@Data
public class LoggerDto {
	private String action;
	private String timestamp;
	private String details;
	private String status;
	private String errorMessage;

	// Add any other fields you need for logging

}
