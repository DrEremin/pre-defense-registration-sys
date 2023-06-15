package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TeacherResponseDto extends PersonResponseDto{
	
	@JsonProperty(value = "jobTitle")
	private final String jobTitle;
	
	
	@JsonCreator
	public TeacherResponseDto(
			long id, 
			String lastName, 
			String firstName, 
			String patronymic, 
			String jobTitle, 
			String email, 
			String login) {
		super(id, login, lastName, firstName, patronymic, email);
		this.jobTitle = jobTitle;
	}
}
