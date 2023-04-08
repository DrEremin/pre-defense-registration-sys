package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDto {
	@JsonProperty(value = "login")
	@NotEmpty
	@Size(min = 2, max = 20)
	protected final String login;
	
	@JsonCreator
	public LoginDto(String login) { this.login = login; }
	
	public String getLogin() { return login; }
}
