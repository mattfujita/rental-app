package com.libertymutual.goforcode.spark.app.controllers;

import static spark.Spark.notFound;

import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.JsonHelper;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class SessionApiController {

	public static final Route create = (Request req, Response res) -> {
		String userJson = req.body();
		Map map = JsonHelper.toMap(userJson);
		String email = map.get("email").toString();
		String password = map.get("password").toString();
		res.header("Content-Type", "application/json");
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			
			User user = User.findFirst("email = ?", email);
			
			if(user != null && BCrypt.checkpw(password, user.getPassword())) {
				req.session().attribute("currentUser", user);
				res.status(201);
				return user.toJson(true);
			}
			res.status(200);
			return "{}";
		}
			
	};
	
	public static final Route destroy = (Request req, Response res) -> {
		req.session().removeAttribute("currentUser");
		res.header("Content-Type", "application/json");
		res.status(200);
		return "{}";
	};

}
