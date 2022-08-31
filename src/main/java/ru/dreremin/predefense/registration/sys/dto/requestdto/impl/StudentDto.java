package ru.dreremin.predefense.registration.sys.dto.requestdto.impl;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import ru.dreremin.predefense.registration.sys.dto.requestdto.PersonDto;


@Getter
public class StudentDto extends PersonDto {
	
	@JsonProperty(value = "studyDirection")
	@NotEmpty
	private final String studyDirection;
	
	@JsonProperty(value = "studyType")
	@NotEmpty
	@Size(max = 20)
	private final String studyType;
	
	@JsonProperty(value = "groupNumber")
	@NotEmpty
	@Size(max = 10)
	private final String groupNumber;
	
	public StudentDto (String lastName,
			   		   String firstName,
			   		   String patronymic,
			   		   String email,
			   		   String login,
			   		   String password,
			   		   String studyDirection,
			   		   String studyType,
			   		   String groupNumber) {
		super(lastName, firstName, patronymic, email, login, password);
		this.studyDirection = studyDirection;
		this.studyType = studyType;
		this.groupNumber = groupNumber;
		
	}
}
