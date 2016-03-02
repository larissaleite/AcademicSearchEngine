package fr.ufrt.searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.daos.interfaces.IUserDAO;
import fr.ufrt.searchengine.models.User;

@Controller
@Scope("request")
public class LoginBean {
	
	private User user;
	
	@Autowired
	private IUserDAO userDAO;
	
	public LoginBean() {
		user = new User();
	}

	public String login() {
		System.out.println("user = " + this.user.getUsername() + "--" + this.user.getPassword());
		
		boolean credentials = userDAO.checkCredentials(user);
		
		if (credentials) {
			return "search.jsf";
		}
		return "index.jsf";
	}	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
