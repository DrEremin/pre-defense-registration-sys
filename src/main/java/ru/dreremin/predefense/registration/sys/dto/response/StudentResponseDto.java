package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentResponseDto {

	@JsonProperty(value = "id")
	private final long id;
	
	@JsonProperty(value = "lastName")
	private final String lastName;
	
	@JsonProperty(value = "firstName")
	private final String firstName;
	
	@JsonProperty(value = "patronymic")
	private final String patronymic;
	
	@JsonProperty(value = "studyDirection")
	private final String studyDirection;
	
	@JsonProperty(value = "studyType")
	private final String studyType;
	
	@JsonProperty(value = "group")
	private final String group;
	
	@JsonProperty(value = "email")
	private final String email;
	
	@JsonProperty(value = "login")
	private final String login;
}
