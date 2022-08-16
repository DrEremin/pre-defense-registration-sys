package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoteDto {

	@NotNull
	private Integer comissionId;
	
	@NotNull
	@NotEmpty
	private String noteContent;
	
}
