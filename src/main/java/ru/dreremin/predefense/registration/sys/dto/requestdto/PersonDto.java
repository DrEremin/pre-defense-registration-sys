package ru.dreremin.predefense.registration.sys.dto.requestdto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class PersonDto {
	
	@JsonProperty(value = "lastName")
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String lastName;
	
	@JsonProperty(value = "firstName")
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String firstName;
	
	@JsonProperty(value = "patronymic")
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String patronymic;
	
	@JsonProperty(value = "email")
	@NotEmpty
	@Email(message = "Invalid format email")
	@Size(max = 40)
	private final String email;
	
	@JsonProperty(value = "login")
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String login;
	
	@JsonProperty(value = "password")
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String password;
} 
