package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserResponseDto extends AdministratorResponseDto {
	
	@JsonProperty(value = "role")
	private final String role;
	
	@JsonCreator
	public UserResponseDto(long id, String login, String role) {
		super(id, login);
		this.role = role;
	}
}
