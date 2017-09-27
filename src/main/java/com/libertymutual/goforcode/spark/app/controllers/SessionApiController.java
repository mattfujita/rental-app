package com.libertymutual.goforcode.spark.app.controllers;

import java.util.Map;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.JsonHelper;

import spark.Request;
import spark.Response;
import spark.Route;

public class SessionApiController {

	public static final Route create = (Request req, Response res) -> {
		String userJson = req.body();
		Map map = JsonHelper.toMap(userJson);
		User user = new User();
		user.fromMap(map);
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			if(user != null) {
				res.header("Content-Type", "application/json");
				return user.toJson(true);
			}
			return null;
		}
		
	};

}
