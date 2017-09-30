package com.libertymutual.goforcode.spark.app.controllers;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.ApartmentsUsers;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.JsonHelper;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import static spark.Spark.notFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentApiController {
	
	
	
	public static final Route details = (Request req, Response res) -> {
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			String apartmentJson = req.body();

			Apartment apt = new Apartment();
			apt.fromMap(JsonHelper.toMap(apartmentJson));
			
			Apartment apartment = Apartment.findById(apt.getId());
			System.out.println(apartment);
			
			List<User> users = apartment.getAll(User.class);
			User user = req.session().attribute("currentUser");
			
//			for (User u : users) {
//				if (user != null && (user.getId().equals((u.getId())))) {
//					liked = true;
//				}
//			}
//			
//			if (user != null) {
//				if (Apartment.where("user_id = ? and id = ?", user.getId(), apt.getId()).size() > 0) {
//					owner = true;
//				}
//			} 
			
			if(apartment != null) {
				res.header("Content-Type", "application/json");
				return apartment.toJson(true);
			}
			notFound("Did not find that.");
			return "";
		}
	};
	
	public static final Route create = (Request req, Response res) -> {

			try (AutoCloseableDb db = new AutoCloseableDb()) {
				
				String apartmentJson = req.body();
				System.out.println(apartmentJson);
				Map map = JsonHelper.toMap(apartmentJson);
			
				Apartment apartment = new Apartment();
				apartment.fromMap(map);
				
				User user = req.session().attribute("currentUser");

				apartment.setIsActive(true);
				user.add(apartment);
				apartment.saveIt();
				
				res.header("Content-Type", "application/json");
				return apartment.toJson(true); 
				
			}

		
	};

	public static final Route index = (Request req, Response res) -> {
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			
			User user = req.session().attribute("currentUser");

			LazyList<Apartment> apartments = Apartment.findAll();
			res.header("Content-Type", "application/json");
			return apartments.toJson(true);
		}
		
	};
	
	public static final Route mine = (Request req, Response res) -> {

		try(AutoCloseableDb db = new AutoCloseableDb()) {
			User user = req.session().attribute("currentUser");
			System.out.println(user);
			LazyList<Apartment> apartments = Apartment.where("user_id = ?", user.getId());
			res.header("Content-Type", "application/json");
			return apartments.toJson(true);
		}
		
	};

		public static final Route update = (Request req, Response res) -> {
		
			Apartment tempApartment = new Apartment();
			String apartmentJson = req.body();
			tempApartment.fromMap(JsonHelper.toMap(apartmentJson));
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			
			Apartment apartment = Apartment.findById(tempApartment.getId());
			System.out.println(apartment);

			if(apartment.getIsActive() == false) { 
				apartment.setIsActive(true);
			} else {
				apartment.setIsActive(false);
			}
			
			apartment.saveIt();
			res.header("Content-Type", "application/json");
			return apartment.toJson(true);
		}
	};
	
	public static final Route createLike = (Request req, Response res) -> {
		String apartmentId = req.body();
		Map map = JsonHelper.toMap(apartmentId);
		int aptId = (int) map.get("apartment_id");
		User user = req.session().attribute("currentUser");
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(aptId);
			apartment.add(user);
			res.header("Content-Type", "application/json");
			return apartment.toJson(true);
		}
	};

	public static final Route getLikes = (Request req, Response res) -> {
		
		String apartmentId = req.body();
		System.out.println(apartmentId);
		Map map = JsonHelper.toMap(apartmentId);
		Apartment tempApartment = new Apartment();
		tempApartment.fromMap(JsonHelper.toMap(apartmentId));
		int aptId = (int) map.get("apartment_id");
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			
			LazyList<Model> count = ApartmentsUsers.where("apartment_id = ?", aptId);
			System.out.println(count);
			res.header("Content-Type", "application/json");
			return count.toJson(true);
		}
		
	};
}
