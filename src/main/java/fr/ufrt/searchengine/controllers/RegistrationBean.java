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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
