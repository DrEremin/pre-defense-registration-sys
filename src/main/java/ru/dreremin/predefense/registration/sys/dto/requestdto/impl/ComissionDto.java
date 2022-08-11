package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ru.dreremin.predefense.registration.sys.exceptions.NegativeTimePeriodException;

public class ComissionDto {
	
	//public static final String rgx = "20[2-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9][+-][0-1][0-9]:[034][05]\\[[A-Za-z]/[A-Za-z]\\]";
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
	
	public ComissionDto(ZonedDateTime startDateTime, 
						ZonedDateTime endDateTime, 
						Boolean presenceFormat, 
						String studyDirection, 
						String location) /*throws DateTimeParseException*/ {
		/*
		this.startTimestamp = ZonedDateTime.parse(
					startDateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		this.endTimestamp = ZonedDateTime.parse(
					endDateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME);*/
		this.startTimestamp = startDateTime;
		this.endTimestamp = endDateTime;
		this.presenceFormat = presenceFormat;
		this.studyDirection = studyDirection;
		this.location = location;
	}
	 
	public ZonedDateTime getStartTimestamp() { return this.startTimestamp; }
	
	public ZonedDateTime getEndTimestamp() { return this.endTimestamp; }
	
	public boolean getPresenceFormat() { return this.presenceFormat; }
	
	public String getStudyDirection() { return this.studyDirection; }
	
	public String getLocation() { return this.location; }
	
	public void periodValidation() throws NegativeTimePeriodException {
		if (this.endTimestamp.toLocalDateTime().compareTo(
				this.startTimestamp.toLocalDateTime()) < 0) {
			throw new NegativeTimePeriodException(
					"The end date-time is earlier than start date-time");
		}
		
	}
}
