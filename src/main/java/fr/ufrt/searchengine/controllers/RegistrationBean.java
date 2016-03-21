package fr.ufrt.searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.daos.interfaces.IUserDAO;
import fr.ufrt.searchengine.models.User;

@Controller
@Scope("request")
public class RegistrationBean {
	
	private User user;
	private String[] areasOfInterest;
	
	public RegistrationBean() {
		user = new User();
	}
	
	@Autowired
	private IUserDAO userDAO;

	public String signUp() {
		userDAO.register(getUser());
		
		//treat errors
		return "index.jsf";
	}
	
	public String[] getAreasOfInterest() {
		
		areasOfInterest = new String[15];
		
		areasOfInterest[0] = "Machine Learning";
		areasOfInterest[1] = "Natural Language Processing";
		areasOfInterest[2] = "Security and Privacy";
		areasOfInterest[3] = "Theory of Computation";
		areasOfInterest[4] = "Software Engineering";
		areasOfInterest[5] = "Computer Graphics";
		areasOfInterest[6] = "Artificial Intelligence";
		areasOfInterest[7] = "Big Data";
		areasOfInterest[8] = "Computer Architecture";
		areasOfInterest[9] = "Data Management";
		areasOfInterest[10] = "Robotics";
		areasOfInterest[11] = "Human Computer Interaction";
		areasOfInterest[12] = "Data Warehousing";
		areasOfInterest[13] = "Systems and Networking";
		areasOfInterest[14] = "Programming Languages";
		
		return areasOfInterest;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
