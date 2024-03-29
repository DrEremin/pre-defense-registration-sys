package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PasswordRequestDto {

	@JsonProperty(value = "password")
	@NotEmpty
	@Size(min = 2, max = 20)
	@NotNull
	protected final String password;
	
	@JsonCreator
	public PasswordRequestDto(String password) {
		this.password = password;
	}
}
