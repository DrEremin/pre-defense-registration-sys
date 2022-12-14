package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AuthenticationDto {

	@JsonProperty(value = "personLogin")
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	protected final String personLogin;
	
	@JsonProperty(value = "personPassword")
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	protected final String personPassword;
	
	public AuthenticationDto(String personLogin,
			   String personPassword) {
			this.personLogin = personLogin;
			this.personPassword = personPassword;

	}
}
