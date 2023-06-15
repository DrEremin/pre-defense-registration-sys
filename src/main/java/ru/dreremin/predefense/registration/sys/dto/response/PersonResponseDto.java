package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonResponseDto extends AdministratorResponseDto {

	@JsonProperty(value = "lastName")
	protected final String lastName;
	
	@JsonProperty(value = "firstName")
	protected final String firstName;
	
	@JsonProperty(value = "patronymic")
	protected final String patronymic;
	
	@JsonProperty(value = "email")
	protected final String email;
	
	@JsonCreator
	public PersonResponseDto(
			long id, 
			String login, 
			String lastName, 
			String firstName, 
			String patronymic,
			String email) {
		super(id, login);
		this.lastName = lastName;
		this.firstName = firstName;
		this.patronymic = patronymic;
		this.email = email;
	}
}
