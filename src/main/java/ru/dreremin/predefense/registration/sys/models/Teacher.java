package ru.dreremin.predefense.registration.sys.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "teacher")
public class Teacher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_t")
	private int id;
	
	@Column(name = "id_p_tfk")
	private long personId;
	
	@Column(name = "job_title")
	private String jobTitle;
	
	public Teacher() {}
	
	public Teacher(int id, long personId, String jobTitle) {
		this.id = id;
		this.personId = personId;
		this.jobTitle = jobTitle;
	}
	
	public Teacher(long personId, String jobTitle) {
		this.personId = personId;
		this.jobTitle = jobTitle;
	}
}
