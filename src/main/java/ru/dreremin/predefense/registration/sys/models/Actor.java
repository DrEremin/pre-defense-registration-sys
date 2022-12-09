package ru.dreremin.predefense.registration.sys.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Entity
@Table(name = "actor")
public class Actor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_auth")
	private long id;
	
	@Column(name = "login", length = 20)
	private String login;
	
	@Column(name = "pswrd", length = 20)
	private String password;
	
	@Column(name = "authority")
	private String role;
	
	public Actor() {}
	
	public Actor(String login, String password, String role) {
		this.login = login;
		this.password = password;
		this.role = role;
	}
} 
 