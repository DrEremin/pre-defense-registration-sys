package ru.dreremin.predefense.registration.sys.util.enums;

public enum Role {
	
	ADMIN("ROLE_ADMIN"), 
	STUDENT("ROLE_STUDENT"), 
	TEACHER("ROLE_TEACHER");
	
	private String role;
	
	Role(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
}
