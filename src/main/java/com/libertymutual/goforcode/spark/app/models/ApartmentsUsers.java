package com.libertymutual.goforcode.spark.app.models;

import org.javalite.activejdbc.Model;

public class ApartmentsUsers extends Model{
	
	public ApartmentsUsers() {}
	
	public ApartmentsUsers(Long user_id, Long apartment_id) {
		
	}
	
	public Long getUserId() {
		return getLong("user_id");
	}

	public void setUserId(Long user_id) {
		set("user_id", user_id);
	}
	
	public Long getApartmentId() {
		return getLong("user_id");
	}

	public void setApartmentId(Long apartment_id) {
		set("apartment_id", apartment_id);
	}
	
}
