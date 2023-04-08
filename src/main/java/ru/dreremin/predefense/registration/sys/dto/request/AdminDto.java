package ru.dreremin.predefense.registration.sys.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public class AdminDto extends AuthenticationDto {

	@JsonCreator
	public AdminDto(String login, String password) { super(login, password); }
}
