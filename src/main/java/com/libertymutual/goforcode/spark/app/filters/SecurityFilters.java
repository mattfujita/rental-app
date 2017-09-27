package com.libertymutual.goforcode.spark.app.filters;

import static spark.Spark.halt;

import java.util.UUID;

import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Session;

public class SecurityFilters {

	public static final Filter isAuthenticated = (Request req, Response res) -> {
		if (req.session().attribute("currentUser") == null) {
            res.redirect("/login?returnPath=" + req.pathInfo());
            halt();
        }
	};
	
	public static final Filter newSession = (Request req, Response res) -> {
		if(req.session().isNew()) {
			String csrf = UUID.randomUUID().toString();
		}
	};
	
//	public static final Filter csrfAuthentication = (Request req, Response res) -> {

//		req.session().attribute("csrf", csrf);
//		
//	    if (!req.session().attribute("csrf").equals(csrf)) {
//	        res.redirect("/login?returnPath=" + req.pathInfo());
//	        halt();
//	    }
//	
//	};

}
