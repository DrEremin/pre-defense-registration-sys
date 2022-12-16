package ru.dreremin.predefense.registration.sys.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "student_comission")
public class StudentCommission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_sc")
	private long id;
	
	@Column(name = "id_s_scfk")
	private long studentId;
	
	@Column(name = "id_c_scfk")
	private int commissionId;
	
	public StudentCommission() {}
	
	public StudentCommission(long studentId, int commissionId) {
		this.studentId = studentId;
		this.commissionId = commissionId;
	}
}
