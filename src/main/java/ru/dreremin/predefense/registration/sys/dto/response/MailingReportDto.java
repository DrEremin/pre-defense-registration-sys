package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class MailingReportDto extends StatusDto {
	
	@JsonProperty(value = "email")
	private final String emailAddress;
	
	@JsonCreator
	public MailingReportDto(int status, String message, String emailAddress) {
		super(status, message);
		this.emailAddress = emailAddress;
	}
}
