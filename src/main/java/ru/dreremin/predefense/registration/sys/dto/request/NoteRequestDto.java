package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NoteRequestDto {

	@JsonProperty(value = "comissionId")
	@NotNull
	private final Integer commissionId;
	
	@JsonProperty(value = "content")
	@NotNull
	private final String content;
	
	public NoteRequestDto(Integer commissionId, String noteContent) {
		this.commissionId = commissionId;
		this.content = noteContent.strip();
	}
}
