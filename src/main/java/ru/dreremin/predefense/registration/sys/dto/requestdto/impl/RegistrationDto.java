package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class RegistrationDto {

	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	private String personLogin;
	
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	private String personPassword;
	
	@NotNull
	private Integer comissionId;
	
	public RegistrationDto(String personLogin,
						   String personPassword,
						   Integer comissionId) {
		this.personLogin = personLogin;
		this.personPassword = personPassword;
		this.comissionId = comissionId;
	}
}
