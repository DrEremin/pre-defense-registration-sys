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
@Table(name = "person")
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_p")
	private long id;
	
	@Column(name = "lastname", length = 20)
	private String lastName;
	
	@Column(name = "firstname", length = 20)
	private String firstName;
	
	@Column(name = "patronymic", length = 20)
	private String patronymic;
	
	@Column(name = "id_auth_pfk")
	private long actorId;
	
	public Person() {}
	
	public Person(
			String lastName, 
			String firstName, 
			String patronymic, 
			long actorId) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.patronymic = patronymic;
		this.actorId = actorId;
	}
	
	@Override
	public String toString() {
		return String.format(
				"%d %s %s %s", 
				id, 
				lastName, 
				firstName, 
				patronymic);
	}
}
