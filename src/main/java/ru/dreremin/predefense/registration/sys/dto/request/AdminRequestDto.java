package ru.dreremin.predefense.registration.sys.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class AdminRequestDto extends AuthenticationRequestDto {

	@JsonCreator
	public AdminRequestDto(String login, String password) { 
		super(login, password); 
	}
}
