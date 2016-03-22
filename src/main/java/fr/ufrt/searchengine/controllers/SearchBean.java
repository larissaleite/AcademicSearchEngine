package fr.ufrt.searchengine.controllers;

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
	
	public SearchBean() {
		solrSearcher = new SolrSearcher();
	}

	
	public void search() {
		List<Item> results = solrSearcher.search(query);
		
		user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		
		List<Author> authors = user.getPreferredAuthors();
		
		for (Item item : results) {
			if (item.getAuthors() != null){
				for (Author author : authors) {
					if (item.getAuthors().get(0).contains(author.getName())) {
						item.setAuthorPreferred(true);
					}
				}
			}
		}
		setItems(results);
		/*setResults(searcherService.getPapers(query));
		*/
	}
	
	public void saveInteraction (String id) {
		user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		
		Interaction interaction = new Interaction();
		interaction.setDocument(id);
		interaction.setUser(user);
		
		interactionDAO.register(interaction);
		
		//item item similarity
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

}
