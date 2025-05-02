package com.doubtshare.seviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doubtshare.dto.LoggerDto;
import com.doubtshare.entity.LoggerEntity;
import com.doubtshare.repository.LoggerRepository;
import com.doubtshare.service.LoggerService;
@Service
public class LoggerServiceImpl implements LoggerService {
	@Autowired
	private  LoggerRepository loggerRepository;
	@Override
	public void createLog(LoggerDto loggerDto) {
		LoggerEntity loggerEntity = LoggerEntity.builder()
			
				.action(loggerDto.getAction())
				.timestamp(loggerDto.getTimestamp())
				.details(loggerDto.getDetails())
				.status(loggerDto.getStatus())
				.errorMessage(loggerDto.getErrorMessage())
				.build();
		
		// Save the loggerEntity to the database using the repository
		 loggerRepository.save(loggerEntity);
		
		

	}

	@Override
	public List<LoggerDto> getAllLogs() {
        // Fetch all logs from the database using the repository
        List<LoggerEntity> loggerEntities = loggerRepository.findAll();
        List<LoggerDto> loggerDtos = new ArrayList<>();
		for (LoggerEntity loggerEntity : loggerEntities) {
			LoggerDto loggerDto = new LoggerDto();
			loggerDto.setAction(loggerEntity.getAction());
			loggerDto.setTimestamp(loggerEntity.getTimestamp());
			loggerDto.setDetails(loggerEntity.getDetails());
			loggerDto.setStatus(loggerEntity.getStatus());
			loggerDto.setErrorMessage(loggerEntity.getErrorMessage());

			loggerDtos.add(loggerDto);
		}
		return loggerDtos;
	}

}
