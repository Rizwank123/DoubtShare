package com.doubtshare.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDoubtRequest {
	 @JsonProperty("subject")
	 private String subject;
	 @NotBlank(message = "Doubt description cannot be blank")
	 @JsonProperty("doubtDescription")
	 private String doubtDescription;
}