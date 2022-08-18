package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;

@Getter
public class AuthorizationDto {

	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	protected String personLogin;
	
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	protected String personPassword;
	
	public AuthorizationDto(String personLogin,
			   String personPassword) {
			this.personLogin = personLogin;
			this.personPassword = personPassword;

	}
}
