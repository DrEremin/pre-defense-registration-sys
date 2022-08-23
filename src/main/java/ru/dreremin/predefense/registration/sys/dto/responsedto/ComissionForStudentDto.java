package ru.dreremin.predefense.registration.sys.dto.responsedto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import ru.dreremin.predefense.registration.sys.models.Comission;


@RequiredArgsConstructor
public class ComissionForStudentDto implements Serializable {

	private final LocalDate date;
	
	private final LocalTime startTime;
	
	private final LocalTime endTime;
	
	private final String studyDirection;
	
	private final String location;
	
	private final List<StudentDto> students;
	
	public ComissionForStudentDto(Comission comission,
								  List<StudentDto> students) {
		this.date = comission.getStartDateTime().toLocalDate();
		this.startTime = comission.getStartDateTime().toLocalTime();
		this.endTime = comission.getEndDateTime().toLocalTime();
		this.studyDirection = comission.getStudyDirection();
		this.location = comission.getLocation();
		this.students = students;
	}
}
