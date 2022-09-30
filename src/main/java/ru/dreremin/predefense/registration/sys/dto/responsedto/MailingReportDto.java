package ru.dreremin.predefense.registration.sys.dto.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MailingReportDto extends StatusDto {
	
	@JsonProperty(value = "email")
	private final String emailAddress;
	
	public MailingReportDto(int status, String message, String emailAddress) {
		super(status, message);
		this.emailAddress = emailAddress;
	}
}
