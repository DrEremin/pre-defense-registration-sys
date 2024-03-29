package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AuthenticationRequestDto extends PasswordRequestDto{
	
	@JsonProperty(value = "login")
	@NotEmpty
	@NotNull
	@Size(min = 2, max = 20)
	protected final String login;
	
	@JsonCreator
	public AuthenticationRequestDto(String login, String password) {
		super(password);
		this.login = login;
	}
}
