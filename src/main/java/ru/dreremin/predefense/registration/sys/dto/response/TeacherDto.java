package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeacherDto {

	@JsonProperty(value = "lastName")
	private final String lastName;
	
	@JsonProperty(value = "firstName")
	private final String firstName;
	
	@JsonProperty(value = "patronymic")
	private final String patronymic;
	
	@JsonProperty(value = "jobTitle")
	private final String jobTitle;
	
	@JsonProperty(value = "email")
	private final String email;
	
	@JsonProperty(value = "login")
	private final String login;
}
