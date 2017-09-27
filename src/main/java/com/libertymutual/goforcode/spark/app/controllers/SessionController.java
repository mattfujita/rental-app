package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class SessionController {
	
	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		model.put("returnPath", req.queryParams("returnPath"));
		
		return MustacheRenderer.getInstance().render("session/login.html", model);
	};
	
	public static final Route create = (Request req, Response res) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User user = User.findFirst("email = ?", email);
			if(user != null && BCrypt.checkpw(password, user.getPassword())) {
				req.session().attribute("currentUser", user);	

			}
		}
		res.redirect(req.queryParamOrDefault("returnPath", "/"));
		return "";
	};
	
	public static final Route destroy = (Request req, Response res) -> {
		req.session().removeAttribute("currentUser");
		res.redirect("/");
		return MustacheRenderer.getInstance().render("home/index.html", null);
	};

}
