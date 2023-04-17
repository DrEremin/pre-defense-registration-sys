package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PersonRequestDto extends AuthenticationRequestDto{
	
	@JsonProperty(value = "lastName")
	@NotEmpty
	@Size(min = 2, max = 20)
	protected final String lastName;
	
	@JsonProperty(value = "firstName")
	@NotEmpty
	@Size(min = 2, max = 20)
	protected final String firstName;
	
	@JsonProperty(value = "patronymic")
	@NotEmpty
	@Size(min = 2, max = 20)
	protected final String patronymic;
	
	@JsonProperty(value = "email")
	@NotEmpty
	@Email(message = "Invalid format email")
	@Size(max = 40)
	protected final String email;
	
	@JsonCreator
	public PersonRequestDto(
			String login,
			String password,
			String lastName, 
			String firstName, 
			String patronymic, 
			String email) {
		super (login, password);
		this.lastName = lastName;
		this.firstName = firstName;
		this.patronymic = patronymic;
		this.email = email;
	}
} 
