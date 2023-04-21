package ru.dreremin.predefense.registration.sys.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.TeacherEntry;

public class CommissionResponseDto {

	@JsonProperty(value = "id")
	protected final int id;
	
	@JsonProperty(value = "date")
	protected final LocalDate date;
	
	@JsonProperty(value = "startTime")
	protected final LocalTime startTime;
	
	@JsonProperty(value = "endTime")
	protected final LocalTime endTime;
	
	@JsonProperty(value = "studyDirection")
	protected final String studyDirection;
	
	@JsonProperty(value = "presenceFormat")
	protected final String presenceFormat;
	
	@JsonProperty(value = "location")
	protected final String location;
	
	@JsonProperty(value = "studentLimit")
	protected final short studentLimit; 
	
	@JsonProperty(value = "teachers")
	protected final List<TeacherEntry> teachers;
	
	@JsonProperty(value = "note")
	protected final String note;
	
	public CommissionResponseDto(Commission commission,
				List<TeacherEntry> teachers, String note) {
		this.id = commission.getId();
		this.date = commission.getStartDateTime().toLocalDate();
		this.startTime = commission.getStartDateTime().toLocalTime();
		this.endTime = commission.getEndDateTime().toLocalTime();
		this.studyDirection = commission.getStudyDirection();
		this.presenceFormat = commission.getPresenceFormat();
		this.location = commission.getLocation();
		this.studentLimit = commission.getStudentLimit();
		this.teachers = teachers;
		this.note = note;
	}
}
