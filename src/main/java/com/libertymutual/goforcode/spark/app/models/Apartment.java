package com.libertymutual.goforcode.spark.app.models;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

public class Apartment extends Model {
	
	public Apartment() {}
	
	public Apartment(int squareFootage, int numberOfBedrooms, double numberOfBathrooms, String address, String city,
			String state, String zipCode, int rent) {
		
		setNumberOfBathrooms(numberOfBathrooms);
		setNumberOfBedrooms(numberOfBedrooms);
		setSquareFootage(squareFootage);
		setAddress(address);
		setCity(city);
		setState(state);
		setZip(zipCode);
		setRent(rent);
		
	}
	
	public int getNumberOfBedrooms() {
		return getInteger("number_of_bedrooms");
	}

	public void setNumberOfBedrooms(int numberOfBedrooms) {
		set("number_of_bedrooms", numberOfBedrooms);
	}
	public int getSquareFootage() {
		return getInteger("square_footage");
	}
	public void setSquareFootage(int squareFootage) {
		set("square_footage", squareFootage);
	}

	public double getNumberOfBathrooms() {
		return getDouble("number_of_bathrooms");
	}
	public void setNumberOfBathrooms(double numberOfBathrooms) {
		set("number_of_bathrooms", numberOfBathrooms);
	}
	public String getAddress() {
		return getString("address");
	}
	public void setAddress(String address) {
		set("address", address);
	}
	public String getCity() {
		return getString("city");
	}
	public void setCity(String city) {
		set("city", city);
	}
	public String getZip() {
		return getString("zip_code");
	}
	public void setZip(String zipCode) {
		set("zip_code", zipCode);
	}

	public int getRent() {
		return getInteger("rent");
	}

	public void setRent(int rent) {
		set("rent", rent);
	}

	public String getState() {
		return getString("state");
	}

	public void setState(String state) {
		set("state", state);
	}
	
	public boolean getIsActive() {
		return getBoolean("is_active");
	}
	
	public void setIsActive(boolean isActive) {
		set("is_active", isActive);
	}
	
}
