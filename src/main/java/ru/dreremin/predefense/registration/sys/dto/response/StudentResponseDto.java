package ru.dreremin.predefense.registration.sys.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentResponseDto extends PersonResponseDto {
	
	@JsonProperty(value = "studyDirection")
	private final String studyDirection;
	
	@JsonProperty(value = "studyType")
	private final String studyType;
	
	@JsonProperty(value = "group")
	private final String group;
	
	@JsonCreator
	public StudentResponseDto(
			long id, 
			String lastName, 
			String firstName, 
			String patronymic,
			String studyDirection,
			String studyType,
			String group,
			String email,
			String login) {
		super(id, login, lastName, firstName, patronymic, email);
		this.studyDirection = studyDirection;
		this.studyType = studyType;
		this.group = group;
	}
}
