package com.libertymutual.goforcode.spark.app;
import static spark.Spark.*;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
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
		
		//the before will verify a user is logged in before trying to add a new apartment
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
		post("/login", SessionController.create);
		get("/signup", UserController.newForm);
		post("/logout", SessionController.destroy);
		
		path("/api", () -> {
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);
			get("/users/:id", UserApiController.details);
			post("/users", UserApiController.create);
		});
		
	}

}
