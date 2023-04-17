package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AuthenticationRequestDto {
	
	@JsonProperty(value = "login")
	@NotEmpty
	@Size(min = 2, max = 20)
	protected final String login;
	
	@JsonProperty(value = "password")
	@NotEmpty
	@Size(min = 2, max = 20)
	protected final String password;
	
	@JsonCreator
	public AuthenticationRequestDto(String login, String password) {
		this.login = login;
		this.password = password;
	}
}
