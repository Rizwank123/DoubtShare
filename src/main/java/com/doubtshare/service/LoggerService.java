package com.doubtshare.service;

import java.util.List;

import com.doubtshare.dto.LoggerDto;

public interface LoggerService {
	public void createLog(LoggerDto loggerDto);
	public List<LoggerDto> getAllLogs();

}
