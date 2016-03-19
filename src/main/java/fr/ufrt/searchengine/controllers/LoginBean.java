package fr.ufrt.searchengine.controllers;

import javax.faces.context.FacesContext;

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
		User registeredUser = userDAO.checkCredentials(user);

		if (registeredUser != null) {
			if (FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().get("user") == null)
				FacesContext.getCurrentInstance().getExternalContext()
						.getSessionMap().put("user", registeredUser);

			return "search.jsf";
		}
		return "index.jsf";
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext()
				.invalidateSession();

		return "index.jsf";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
