package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.ApartmentsUsers;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {

	public static final Route details = (Request req, Response res) -> {
		
		boolean liked = false;
		boolean owner = false;
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			
			int apartmentId = Integer.parseInt(req.params("id"));
			Apartment apartment = Apartment.findById(apartmentId);
			
			List<User> users = apartment.getAll(User.class); //being used for the many to many likes
			User user = req.session().attribute("currentUser");

			for(User u : users) {
					if (user != null && (user.getId().equals(u.getId()))) {
						liked = true;
					}
			}
			
			if (user != null) {
				if(Apartment.where("user_id = ? and id = ?", user.getId(), apartmentId).size() > 0) {
					owner = true;
				}
			}
			 
			Map<String, Object> model = new HashMap<String, Object>(); //model and view for spark
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			model.put("apartment", apartment);
			model.put("owner", owner);
			model.put("liked", !liked && !owner && user != null);
			model.put("counter", ApartmentsUsers.count("apartment_id = ?", apartmentId));
			model.put("inactiveApartment", !apartment.getIsActive());
			model.put("returnPath", req.queryParams("returnPath"));

			return MustacheRenderer.getInstance().render("apartment/details.html", model);
		}
	};
	
	public static final Route newForm = (Request req, Response res) -> {
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		
		return MustacheRenderer.getInstance().render("apartment/newForm.html", model);
	};

	public static final Route create = (Request req, Response res) -> {
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
				Apartment apartment = new Apartment(
					Integer.parseInt(req.queryParams("square_footage")),
					Integer.parseInt(req.queryParams("number_of_bedrooms")),
					Double.parseDouble(req.queryParams("number_of_bathrooms")),
					req.queryParams("address"),
					req.queryParams("city"),
					req.queryParams("state"),
					req.queryParams("zip_code"),
					Integer.parseInt(req.queryParams("rent"))			
				);
			User user = req.session().attribute("currentUser");
					
			apartment.setIsActive(true);
			user.add(apartment);
			apartment.saveIt();
			res.redirect("/");
			return "";
		}
	};

	public static final Route index = (Request req, Response res) -> {
		User user = req.session().attribute("currentUser");
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			List<Apartment> activeApartments = Apartment.where("is_active = ? and user_id = ?", true, user.getId());
			List<Apartment> inactiveApartments = Apartment.where("is_active = ? and user_id = ?", false, user.getId());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("activeApartments", activeApartments);
			model.put("inactiveApartments", inactiveApartments);
			
			return MustacheRenderer.getInstance().render("apartment/index.html", model);
		}
	};

	public static final Route update = (Request req, Response res) -> {
		
		boolean change;
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			
			Map<String, Object> model = new HashMap<String, Object>();
			
			Apartment apartment = Apartment.findById(Integer.parseInt(req.params("id")));
			change = apartment.getIsActive();
			
			if(apartment.getIsActive() == false) { 
				apartment.setIsActive(!change);
			}
			
			if(apartment.getIsActive() == true) {
				apartment.setIsActive(!change);
			}
			
			apartment.saveIt();
			
			res.redirect("/apartments/" + req.params("id"));
			return "";
		}
	};

	public static final Route createLike = (Request req, Response res) -> {
		int apartmentId = Integer.parseInt(req.params("id"));
		User user = req.session().attribute("currentUser");
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(apartmentId);
			apartment.add(user);
			res.redirect("/apartments/" + req.params("id"));
			return "";
		}
	};
}
