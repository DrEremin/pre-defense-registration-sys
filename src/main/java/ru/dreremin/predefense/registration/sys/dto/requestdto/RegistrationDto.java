package ru.dreremin.predefense.registration.sys.dto.requestdto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class RegistrationDto extends AuthenticationDto {

	@JsonProperty(value = "comissionId")
	@NotNull
	private final Integer comissionId;
	
	public RegistrationDto(String personLogin,
						   String personPassword,
						   Integer comissionId) {
		super(personLogin, personPassword);
		this.comissionId = comissionId;
	}
}
