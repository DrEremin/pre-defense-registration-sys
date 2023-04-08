package ru.dreremin.predefense.registration.sys.dto.response;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.dreremin.predefense.registration.sys.models.Commission;
import ru.dreremin.predefense.registration.sys.models.TeacherEntry;

public class ActualComissionForTeacherDto 
		extends ActualCommissionForStudentDto {
	
	@JsonProperty(value = "note")
	protected final String note;
	
	public ActualComissionForTeacherDto(Commission commission, 
										List<TeacherEntry> teachers,
										String note) {
		super(commission, teachers);
		this.note = note;
	}
	
	public String getNote() { return note; }
	
	@Override
	public boolean equals(Object other) {
		
		if (this == other) { return true; }
		if (other == null || this.getClass() != other.getClass()) { 
			return false;
		}
		
		ActualComissionForTeacherDto o = (ActualComissionForTeacherDto) other;
		
		return this.studyDirection.equals(o.getStudyDirection()) 
				&& this.date.equals(o.getDate())
				&& this.startTime.equals(o.getStartTime())
				&& this.endTime.equals(o.getEndTime())
				&& this.location.equals(o.getLocation())
				&& this.teachers.equals(o.getTeachers())
				&& this.note.equals(o.getNote())
				&& this.id == o.getId();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id,
							studyDirection, 
							date, 
							startTime, 
							endTime, 
							location, 
							teachers,
							note);
	}
}
