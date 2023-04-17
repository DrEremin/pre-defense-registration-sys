package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdministratorResponseDto {

	@JsonProperty("id")
	private final long id;
	
	@JsonProperty("login")
	private final String login;
}
