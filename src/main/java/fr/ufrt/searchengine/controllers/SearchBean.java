package fr.ufrt.searchengine.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.daos.interfaces.IInteractionDAO;
import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.Interaction;
import fr.ufrt.searchengine.models.Item;
import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.recommender.DocumentRecommenderChainInitializer;
import fr.ufrt.searchengine.recommender.Recommender;
import fr.ufrt.searchengine.recommender.UserClusterRecommender;
import fr.ufrt.searchengine.searcher.SeacherService;
import fr.ufrt.searchengine.searcher.SolrSearcher;

@Controller
@ViewScoped
public class SearchBean {

	private String query;
	private List<Item> items;
	private SolrSearcher solrSearcher;

	@Autowired
	private SeacherService searcherService;

	@Autowired
	private IInteractionDAO interactionDAO;

	private User user;

	private List<String> clusterRecommendations;

	private List<String> documentsRecommendations;

	public SearchBean() {
		solrSearcher = new SolrSearcher();

		clusterRecommendations = new ArrayList<String>();
		documentsRecommendations = new ArrayList<String>();

		// User user = (User)
		// FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		int userId = 1;

		Recommender clusterRecommender = new UserClusterRecommender();
		clusterRecommendations = clusterRecommender.getRecommendations(
				clusterRecommendations, userId);

		Recommender documentRecommender = DocumentRecommenderChainInitializer
				.getChainOfRecommenders();
		documentsRecommendations = documentRecommender.getRecommendations(
				documentsRecommendations, userId);

	}

	public void search() {
		List<Item> results = solrSearcher.search(query);

		user = (User) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("user");

		List<Author> authors = user.getPreferredAuthors();

		for (Item item : results) {
			String[] idSplit = item.getId().split("/");
			String id = idSplit[idSplit.length - 1];

			if (documentsRecommendations.contains(id)) {
				item.setRecommended(true);
			}
			if (item.getAuthors() != null) {
				for (Author author : authors) {
					if (item.getAuthors().get(0).contains(author.getName())) {
						item.setAuthorPreferred(true);
					}
				}
			}
		}

		// Sorting to put recommended ones first on the list
		Collections.sort(results, new Comparator<Item>() {
			@Override
			public int compare(Item doc1, Item doc2) {
				return Boolean.compare(doc2.isRecommended(),
						doc1.isRecommended());
			}
		});

		setItems(results);
	}

	public void saveInteraction(String id) {
		user = (User) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("user");

		Interaction interaction = new Interaction();
		interaction.setDocument(id);
		interaction.setUser(user);

		interactionDAO.register(interaction);

		// item item similarity
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<String> getClusterRecommendations() {
		return clusterRecommendations;
	}

	public void setClusterRecommendations(List<String> clusterRecommendations) {
		this.clusterRecommendations = clusterRecommendations;
	}

	public List<String> getDocumentsRecommendations() {
		return documentsRecommendations;
	}

	public void setDocumentsRecommendations(
			List<String> documentsRecommendations) {
		this.documentsRecommendations = documentsRecommendations;
	}

}
