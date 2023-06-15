package ru.dreremin.predefense.registration.sys.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommissionResponseDto {

	@JsonProperty(value = "id")
	private final int id;
	
	@JsonProperty(value = "startTime")
	private final String startDateTime;
	
	@JsonProperty(value = "endTime")
	private final String endDateTime;
	
	@JsonProperty(value = "studyDirection")
	private final String studyDirection;
	
	@JsonProperty(value = "location")
	private final String location;
	
	@JsonProperty(value = "studentLimit")
	private final short studentLimit; 
	
	@JsonProperty(value = "teachers")
	private final List<TeacherResponseDto> teachers;
	
	@JsonProperty(value = "students")
	private final List<StudentResponseDto> students;
	
	@JsonProperty(value = "note")
	private final String note;
}
