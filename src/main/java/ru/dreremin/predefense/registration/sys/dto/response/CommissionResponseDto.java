package ru.dreremin.predefense.registration.sys.dto.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommissionResponseDto {

	@JsonProperty(value = "id")
	protected final int id;
	
	@JsonProperty(value = "startTime")
	protected final String startDateTime;
	
	@JsonProperty(value = "endTime")
	protected final String endDateTime;
	
	@JsonProperty(value = "studyDirection")
	protected final String studyDirection;
	
	@JsonProperty(value = "presenceFormat")
	protected final String presenceFormat;
	
	@JsonProperty(value = "location")
	protected final String location;
	
	@JsonProperty(value = "studentLimit")
	protected final short studentLimit; 
	
	@JsonProperty(value = "teachers")
	protected final List<TeacherResponseDto> teachers;
	
	@JsonProperty(value = "students")
	protected final List<StudentResponseDto> students;
	
	@JsonProperty(value = "note")
	protected final String note;
}
