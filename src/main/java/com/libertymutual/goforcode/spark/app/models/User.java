package com.libertymutual.goforcode.spark.app.models;

import org.javalite.activejdbc.Model;

public class User extends Model {
	
	public User() {}
	
	public User(String firstName, String lastName, String email, String password) {
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setPassword(password);
	}
	
	public String getFirstName() {
		return getString("first_name");
	}
	public void setFirstName(String firstName) {
		set("first_name", firstName);
	}
	public String getLastName() {
		return getString("last_name");
	}
	public void setLastName(String lastName) {
		set("last_name", lastName);
	}
	public String getEmail() {
		return getString("email");
	}
	public void setEmail(String email) {
		set("email", email);
	}
	public String getPassword() {
		return getString("password");
	}
	public void setPassword(String password) {
		set("password", password);
	}

}
