package com.libertymutual.goforcode.spark.app;
import static spark.Spark.*;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
import com.libertymutual.goforcode.spark.app.controllers.SessionApiController;
import com.libertymutual.goforcode.spark.app.controllers.SessionController;
import com.libertymutual.goforcode.spark.app.controllers.UserApiController;
import com.libertymutual.goforcode.spark.app.controllers.UserController;
import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;
import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.ApartmentsUsers;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;

import spark.Request;
import spark.Response;

public class Application {
	
	public static void main(String[] args) {
		
		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			User matt = new User("Matt", "Test", "test@gmail.com", encryptedPassword);
			matt.saveIt();
			
			Apartment.deleteAll();
			ApartmentsUsers.deleteAll();
			Apartment a = new Apartment(1000, 2, 2, "123 Main St", "Seattle", "WA", "98121", 2800);
			a.setIsActive(true);
			matt.add(a);
			a.saveIt();
			
			Apartment b = new Apartment(800, 2, 1, "345 Cedar St", "Boston", "MA", "12112", 3000);
			matt.add(b);
			b.saveIt();
			
		} 
		enableCORS("*", "*", "*");
		//before("/*", SecurityFilters.newSession);
		
		path("/apartments", () -> {
			before("/new", SecurityFilters.isAuthenticated); 
			get("/new", ApartmentController.newForm);
			
			before("/mine", SecurityFilters.isAuthenticated); 
			get("/mine", ApartmentController.index);
			
			before("/:id/activations", SecurityFilters.isAuthenticated);
			post("/:id/activations", ApartmentController.update);
			
			before("/:id/deactivations", SecurityFilters.isAuthenticated); 
			post("/:id/deactivations", ApartmentController.update);
			before("/:id/like", SecurityFilters.isAuthenticated); 
			post("/:id/like", ApartmentController.createLike);
			
			get("/:id", ApartmentController.details); 	 // /:id is how we get a path variable
			
			before("", SecurityFilters.isAuthenticated); 
			post("", ApartmentController.create);
		});
		
		path("/users", () -> {
			get("/new", UserController.newForm);
			post("", UserController.create);
		});
		
		get("/", HomeController.index);

		get("/login", SessionController.newForm);
		get("/signup", UserController.newForm);

		post("/login", SessionController.create);
		
		post("/logout", SessionController.destroy);
		
		path("/api", () -> {
			get("/apartments", ApartmentApiController.index);
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);
			get("/users/:id", UserApiController.details);
			post("/users", UserApiController.create);
			post("/sessions", SessionApiController.create);
		});
		
	}
	
	// Enables CORS on requests. This method is an initialization method and should be called once.
	private static void enableCORS(final String origin, final String methods, final String headers) {

	    options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", origin);
	        response.header("Access-Control-Request-Method", methods);
	        response.header("Access-Control-Allow-Headers", headers);
	        // Note: this may or may not be necessary in your particular application
	        //response.type("application/json");
	    });
	}

}
