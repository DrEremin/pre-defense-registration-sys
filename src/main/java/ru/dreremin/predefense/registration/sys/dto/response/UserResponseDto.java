package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserResponseDto {

	@JsonProperty(value = "id")
	private final long id;
	
	@JsonProperty(value = "login")
	private final String login;
	
	@JsonProperty(value = "role")
	private final String role;
}
