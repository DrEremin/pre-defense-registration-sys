package ru.dreremin.predefense.registration.sys.models;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "comission")
public class Comission implements Comparable<Comission>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_c")
	private int id;
	
	@Column(name = "start_datetime_c")
	private ZonedDateTime startDateTime;
	
	@Column(name = "end_datetime_c")
	private ZonedDateTime endDateTime;
	
	@Column(name = "presence_format_c")
	private boolean presenceFormat;
	
	@Column(name = "study_direction_c")
	private String studyDirection;
	
	@Column(name = "location_c")
	private String location;
	
	@Column(name = "student_limit")
	private short studentLimit;
	
	public Comission() {}
	
	public Comission(ZonedDateTime startDateTime,
					 ZonedDateTime endDateTime,
					 boolean presenceFormat,
					 String studyDirection,
					 String location,
					 Short studentLimit) {
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.presenceFormat = presenceFormat;
		this.studyDirection = studyDirection;
		this.location = location;
		this.studentLimit = studentLimit;
	}
	
	@Override
	public int compareTo(Comission other) {
		return this.startDateTime.toLocalDateTime().compareTo(
				other.getStartDateTime().toLocalDateTime());
	}
}
