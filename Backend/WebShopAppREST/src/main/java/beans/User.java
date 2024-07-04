package beans;

import enums.UserRole;
import enums.UserStatus;

public class User {
	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String gender;
	private String birthDate;
	private UserRole role;
	private boolean isActive;
	private UserStatus status;
	
	private Factory factory;
	
	public User() {
	}

	public User(String id, String firstName, String lastName, String username, String password, String gender, String birthDate, UserRole role, boolean isActive, UserStatus status) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.birthDate = birthDate;
		this.role = role;
		this.isActive = isActive;
		this.status = status;
		factory = new Factory();
	}


	
	public User(String id2, String firstName2, String lastName2, String username2, String password2, String gender2,
			String birthDate2, UserRole role2, boolean isActive, UserStatus status, Factory factory) {
		super();
		this.id = id2;
		this.firstName = firstName2;
		this.lastName = lastName2;
		this.username = username2;
		this.password = password2;
		this.gender = gender2;
		this.birthDate = birthDate2;
		this.role = role2;
		this.isActive = isActive;
		this.status = status;
		this.factory = factory;
	}

	public Factory getFactory() {
		return factory;
	}

	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
