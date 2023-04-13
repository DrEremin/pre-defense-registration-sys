package ru.dreremin.predefense.registration.sys.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "email")
public class Email {

	@Id
	@Column(name = "boxpk", length = 40)
	private String box;
	
	@Column(name = "id_p_boxfk")
	private long personId;
	
	public Email() {}
}

