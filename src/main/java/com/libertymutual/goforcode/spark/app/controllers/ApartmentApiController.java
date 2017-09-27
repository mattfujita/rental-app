package com.libertymutual.goforcode.spark.app.controllers;

import com.libertymutual.goforcode.spark.app.models.Apartment;
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

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentApiController {
	
	public static final Route details = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			Apartment apartment = Apartment.findById(id);
			
			if(apartment != null) {
				res.header("Content-Type", "application/json");
				return apartment.toJson(true);
			}
			notFound("Did not find that.");
			return "";
		}
	};
	
	public static final Route create = (Request req, Response res) -> {

			String apartmentJson = req.body();
			Map map = JsonHelper.toMap(apartmentJson);
			Apartment apartment = new Apartment();
			apartment.fromMap(map);
		
			try (AutoCloseableDb db = new AutoCloseableDb()) {
				apartment.saveIt();
				res.status(201);
				return apartment.toJson(true);
			}
		
	};

	public static final Route index = (Request req, Response res) -> {
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {

			LazyList<Apartment> activeApartments = Apartment.where("is_active = ?", true);
			res.header("Content-Type", "application/json");
			return activeApartments.toJson(true);
			//List<String> apartments = new ArrayList<String>();
			
//			if(activeApartments != null) {
//				res.header("Content-Type", "application/json");
//				for (Apartment a : activeApartments) {
//					apartments.add(a.toJson(true));
//					return apartments;
//				}
//			}
//			notFound("No active apartments found.");
//			return "";
		}
		
	};
	
}
