package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MailingRequestDto {

	@JsonProperty(value = "subject")
	@NotNull
	@NotEmpty
	private String subject;
	
	@JsonProperty(value = "content")
	@NotNull
	@NotEmpty
	private String content;
	
	public MailingRequestDto (String subject, String content) {
		this.subject = subject;
		this.content = content;
	}
}
