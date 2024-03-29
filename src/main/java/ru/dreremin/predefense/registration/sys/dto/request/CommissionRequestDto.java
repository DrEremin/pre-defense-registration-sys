package ru.dreremin.predefense.registration.sys.dto.request;

import java.time.ZonedDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CommissionRequestDto {
	
	@JsonProperty(value = "startDateTime")
	@NotNull
	private ZonedDateTime startDateTime;
	
	@JsonProperty(value = "endDateTime")
	@NotNull
	private ZonedDateTime endDateTime;
	
	@JsonProperty(value = "studyDirection")
	@NotEmpty
	@NotNull
	private final String studyDirection;
	
	@JsonProperty(value = "location")
	@NotEmpty
	@NotNull
	private final String location;
	
	@JsonProperty(value = "studentLimit")
	@Max(Short.MAX_VALUE)
	@Min(0)
	@NotNull
	private final short studentLimit;
	
	public CommissionRequestDto(
			ZonedDateTime startDateTime, 
			ZonedDateTime endDateTime,  
			String studyDirection, 
			String location,
			Short studentLimit) {
		
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.studyDirection = studyDirection;
		this.location = location;
		this.studentLimit = studentLimit;
	}
	
	public void setStartDateTime(ZonedDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public void setEndDateTime(ZonedDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
}
