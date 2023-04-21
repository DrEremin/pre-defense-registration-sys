package ru.dreremin.predefense.registration.sys.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@Getter
public class StudentRequestDto extends PersonRequestDto {
	
	@JsonProperty(value = "studyDirection")
	@NotEmpty
	private final String studyDirection;
	
	@JsonProperty(value = "studyType")
	@NotEmpty
	@Size(max = 20)
	private final String studyType;
	
	@JsonProperty(value = "group")
	@NotEmpty
	@Size(max = 10)
	private final String group;
	
	public StudentRequestDto (
			String login,
			String password,
			String lastName,
			String firstName,
			String patronymic,
			String email,
			String studyDirection,
			String studyType,
			String group) {
		super(login, password, lastName, firstName, patronymic, email);
		this.studyDirection = studyDirection;
		this.studyType = studyType;
		this.group = group;
		
	}
}
