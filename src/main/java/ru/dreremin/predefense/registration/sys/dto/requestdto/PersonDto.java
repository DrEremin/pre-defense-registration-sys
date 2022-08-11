package ru.dreremin.predefense.registration.sys.dto.requestdto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class PersonDto {
	
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String lastName;
	
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String firstName;
	
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String patronymic;
	
	@NotEmpty
	@Email(message = "sfsf")
	@Size(max = 40)
	private final String email;
	
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String login;
	
	@NotEmpty
	@Size(min = 2, max = 20)
	private final String password;
} 
