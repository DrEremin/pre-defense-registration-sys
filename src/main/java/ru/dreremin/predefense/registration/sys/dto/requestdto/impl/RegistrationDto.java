package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class RegistrationDto extends AuthorizationDto {

	@NotNull
	private Integer comissionId;
	
	public RegistrationDto(String personLogin,
						   String personPassword,
						   Integer comissionId) {
		super(personLogin, personPassword);
		this.comissionId = comissionId;
	}
}
