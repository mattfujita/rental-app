package com.libertymutual.goforcode.spark.app.controllers;

import static spark.Spark.get;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

public class HomeController {
	
	public static final Route index = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()){
			List<Apartment> apartments = Apartment.where("is_active = ?", true);
			
			Map<String, Object> model = new HashMap<String, Object>(); //model and view for spark
			model.put("apartments", apartments);
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			
		    //return new VelocityTemplateEngine().render(new ModelAndView(model, "templates/home/index2.html"));
		  return MustacheRenderer.getInstance().render("home/index.html", model);
		}
	};

}
