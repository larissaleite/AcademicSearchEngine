package fr.ufrt.searchengine.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.daos.interfaces.IUserDAO;
import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.recommender.DocumentRecommenderChainInitializer;
import fr.ufrt.searchengine.recommender.Recommender;

@Controller
@Scope("request")
public class LoginBean {

	private User user;

	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private SearchBean searchBean;
	
	@Autowired
	private HomeBean homeBean;

	public LoginBean() {
		user = new User();
	}

	public String login() {
		User registeredUser = userDAO.checkCredentials(user);

		if (registeredUser != null) {
			instantiateUserInformation(registeredUser);

			return "home.jsf";
		}
		return "index.jsf";
	}
	
	private void instantiateUserInformation(User user) {
		if (FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("user") == null) {
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("user", user);
		}

		searchBean.setUser(user);
		homeBean.setUser(user);
		
		List<String> documentsRecommendations = new ArrayList<String>();

		if (!homeBean.isNewUser(user)) {
		
			Recommender documentRecommender = DocumentRecommenderChainInitializer
					.getChainOfRecommenders();
			documentsRecommendations = documentRecommender.getRecommendations(
					documentsRecommendations, user.getId());
		}
		
		searchBean.setDocumentsRecommendations(documentsRecommendations);
		
		searchBean.mapAuthorsWeight();
		searchBean.mapConferencesWeight();
		
		homeBean.mapDocsNameTitle();
		
		homeBean.setDocumentsRecommendations(documentsRecommendations);
		
		homeBean.setTopDocuments();
		homeBean.mapRecommendedDocsIds();
		homeBean.setTopKeywords();
		
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
