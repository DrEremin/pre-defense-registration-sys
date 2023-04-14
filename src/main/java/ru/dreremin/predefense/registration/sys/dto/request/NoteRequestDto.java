package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoteRequestDto {

	@JsonProperty(value = "comissionId")
	@NotNull
	private final Integer commissionId;
	
	@JsonProperty(value = "noteContent")
	@NotNull
	@NotEmpty
	private final String noteContent;
}