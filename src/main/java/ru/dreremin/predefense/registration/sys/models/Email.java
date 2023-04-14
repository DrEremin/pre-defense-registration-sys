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
@Table(name = "email")
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_e")
	private long id;

	@Column(name = "address", length = 40)
	private String address;
	
	@Column(name = "id_p_boxfk")
	private long personId;
	
	public Email() {}
	
	public Email(String address, long personId) {
		this.address = address;
		this.personId = personId;
	}
}

