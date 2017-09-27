package com.libertymutual.goforcode.spark.app.controllers;

import static spark.Spark.notFound;

import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.JsonHelper;

import spark.Request;
import spark.Response;
import spark.Route;

public class SessionApiController {

	public static final Route create = (Request req, Response res) -> {
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			String userJson = req.body();
			Map map = JsonHelper.toMap(userJson);
			String email = (String) map.get("email");
			String password = (String) map.get("password");
			
			User user = User.findFirst("email = ?", email);
			
			if(user != null && BCrypt.checkpw(password, user.getPassword())) {
				req.session().attribute("currentUser", user);
				res.header("Content-Type", "application/json");
				return user.toJson(true);
			}
			notFound("Did not find user.");
			return "";
		}
			
	};

}
