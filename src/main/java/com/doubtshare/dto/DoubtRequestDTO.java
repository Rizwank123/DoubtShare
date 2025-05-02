package com.doubtshare.dto;

import com.doubtshare.entity.DoubtRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubtRequestDTO {
    private UUID id;
    private UUID studentId;
    private String studentUsername;
    private UUID tutorId;
    private String tutorUsername;
    private String subject;
    private String doubtDescription;
    private DoubtRequest.DoubtStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
}