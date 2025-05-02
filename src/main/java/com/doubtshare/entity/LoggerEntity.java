package com.doubtshare.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "logger")
public class LoggerEntity {
    @Id
    private String id;
    private String action;
    private String timestamp;
    private String details;
    private String status;
    private String errorMessage;
}
