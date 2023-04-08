package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class TeacherDto extends PersonDto{
	
	@JsonProperty(value = "jobTitle")
	@NotEmpty
	private final String jobTitle;
	
	public TeacherDto (
			String login,
			String password,
			String lastName,
			String firstName,
			String patronymic,
			String email,
			String jobTitle) {
		super(login, password, lastName, firstName, patronymic, email);
		this.jobTitle = jobTitle;
	}
} 
