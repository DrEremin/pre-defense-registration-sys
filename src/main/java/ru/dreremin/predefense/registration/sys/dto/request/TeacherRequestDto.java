package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class TeacherRequestDto extends PersonRequestDto{
	
	@JsonProperty(value = "jobTitle")
	@Min(value = 0)
	private final int id;
	
	@JsonProperty(value = "jobTitle")
	@NotEmpty
	@NotNull
	private final String jobTitle;
	
	public TeacherRequestDto (
			int id,
			String login,
			String password,
			String lastName,
			String firstName,
			String patronymic,
			String email,
			String jobTitle) {
		super(login, password, lastName, firstName, patronymic, email);
		this.id = id;
		this.jobTitle = jobTitle;
	}
} 
