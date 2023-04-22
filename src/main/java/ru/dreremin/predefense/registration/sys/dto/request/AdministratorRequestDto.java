package ru.dreremin.predefense.registration.sys.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class AdministratorRequestDto extends AuthenticationRequestDto {

	@JsonCreator
	public AdministratorRequestDto(String login, String password) { 
		super(login, password); 
	}
}
