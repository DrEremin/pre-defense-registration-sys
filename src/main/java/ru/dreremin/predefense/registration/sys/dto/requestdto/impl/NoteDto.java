package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoteDto {

	@NotNull
	private final Integer comissionId;
	
	@NotNull
	@NotEmpty
	private final String noteContent;
	
}
