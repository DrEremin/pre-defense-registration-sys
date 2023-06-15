package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdministratorResponseDto {

	@JsonProperty("id")
	protected final long id;
	
	@JsonProperty("login")
	protected final String login;
}
