package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NoteRequestDto {
	
	@JsonProperty(value = "content")
	@NotNull
	private final String content;
	
	@JsonCreator
	public NoteRequestDto(String content) {
		this.content = content.strip();
	}
}
