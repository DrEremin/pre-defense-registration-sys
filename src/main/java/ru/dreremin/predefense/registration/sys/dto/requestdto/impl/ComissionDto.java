package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ru.dreremin.predefense.registration.sys.exceptions.NegativeTimePeriodException;

public class ComissionDto {
	
	@NotNull
	private final ZonedDateTime startTimestamp;
	@NotNull
	private final ZonedDateTime endTimestamp;
	
	@NotNull
	private final Boolean presenceFormat;
	
	@NotEmpty
	@NotNull
	private final String studyDirection;
	
	@NotEmpty
	@NotNull
	private final String location;
	
	@NotNull
	private final Short studentLimit;
	
	public ComissionDto(ZonedDateTime startDateTime, 
						ZonedDateTime endDateTime, 
						Boolean presenceFormat, 
						String studyDirection, 
						String location,
						Short studentLimit) {
		this.startTimestamp = startDateTime;
		this.endTimestamp = endDateTime;
		this.presenceFormat = presenceFormat;
		this.studyDirection = studyDirection;
		this.location = location;
		this.studentLimit = studentLimit;
	}
	 
	public ZonedDateTime getStartTimestamp() { return this.startTimestamp; }
	
	public ZonedDateTime getEndTimestamp() { return this.endTimestamp; }
	
	public boolean getPresenceFormat() { return this.presenceFormat; }
	
	public String getStudyDirection() { return this.studyDirection; }
	
	public String getLocation() { return this.location; }
	
	public Short getStudentLimit() { return this.studentLimit; }
	
	public void periodValidation() throws NegativeTimePeriodException {
		if (this.endTimestamp.toLocalDateTime().compareTo(
				this.startTimestamp.toLocalDateTime()) < 0) {
			throw new NegativeTimePeriodException(
					"The end date-time is earlier than start date-time");
		}
		
	}
}
