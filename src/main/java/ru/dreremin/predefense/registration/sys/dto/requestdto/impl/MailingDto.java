package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class MailingDto extends AuthenticationDto {

	@JsonProperty(value = "subject")
	@NotNull
	@NotEmpty
	private String subject;
	
	@JsonProperty(value = "content")
	@NotNull
	@NotEmpty
	private String content;
	
	public MailingDto (
			String personLogin,
			String personPassword,
			String subject,
			String content) {
		super(personLogin, personPassword);
		this.subject = subject;
		this.content = content;
	}
}
