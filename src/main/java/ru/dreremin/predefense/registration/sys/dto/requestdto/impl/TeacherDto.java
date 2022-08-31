package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import ru.dreremin.predefense.registration.sys.dto.requestdto.PersonDto;

@Getter
public class TeacherDto extends PersonDto{
	
	@JsonProperty(value = "jobTitle")
	@NotEmpty
	private final String jobTitle;
	
	public TeacherDto (String lastName,
					   String firstName,
					   String patronymic,
					   String email,
					   String login,
					   String password,
					   String jobTitle) {
		super(lastName, firstName, patronymic, email, login, password);
		this.jobTitle = jobTitle;
	}
} 
