package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class NoteRequestDto {

	@JsonProperty(value = "comissionId")
	@NotNull
	private final Integer commissionId;
	
	@JsonProperty(value = "noteContent")
	@NotNull
	private final String noteContent;
	
	public NoteRequestDto(Integer commissionId, String noteContent) {
		this.commissionId = commissionId;
		this.noteContent = noteContent.strip();
	}
}
