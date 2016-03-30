package fr.ufrt.searchengine.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.daos.interfaces.IInteractionDAO;
import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.Interaction;
import fr.ufrt.searchengine.models.Item;
import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.recommender.ItemItemRecommender;
import fr.ufrt.searchengine.recommender.Recommender;
import fr.ufrt.searchengine.recommender.UserKeywordRecommender;
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
	
	private List<String> similarItems;
	
	private HashMap<String, Integer> docIds;
	
	public SearchBean() {
		solrSearcher = new SolrSearcher();

		clusterRecommendations = new ArrayList<String>();

		//just as example
		Recommender clusterRecommender = new UserKeywordRecommender();
		clusterRecommendations = clusterRecommender.getRecommendations(
				clusterRecommendations, 115);
		
		mapDocsIds();

	}

	public void search() {
		List<Item> results = solrSearcher.search(query);

		List<Author> authors = getUser().getPreferredAuthors();

		for (Item item : results) {
			checkIfItemIsRecommended(item);
			checkIfAuthorIsPreferred(authors, item);
		}

		sortResultsByRecommendation(results);

		setItems(results);
	}

	private void sortResultsByRecommendation(List<Item> results) {
		// Sorting to put recommended ones first on the list
		Collections.sort(results, new Comparator<Item>() {
			@Override
			public int compare(Item doc1, Item doc2) {
				return Boolean.compare(doc2.isRecommended(),
						doc1.isRecommended());
			}
		});
	}

	private void checkIfAuthorIsPreferred(List<Author> authors, Item item) {
		if (item.getAuthors() != null) {
			for (Author author : authors) {
				if (item.getAuthors().get(0).contains(author.getName())) {
					item.setAuthorPreferred(true);
				}
			}
		}
	}

	private void checkIfItemIsRecommended(Item item) {
		String[] idSplit = item.getId().split("/");
		String id = idSplit[idSplit.length - 1];

		if (documentsRecommendations.contains(id)) {
			item.setRecommended(true);
		}
	}

	public void saveInteraction(String id) {
		Interaction interaction = new Interaction();
		interaction.setDocument(id);
		interaction.setUser(getUser());

		interactionDAO.register(interaction);

		// item item similarity
		Recommender itemRecommender = new ItemItemRecommender();
		String[] idSplit = id.split("/");
		String doc = idSplit[idSplit.length - 1];
		setSimilarItems(itemRecommender.getRecommendations(new ArrayList<String>(), docIds.get(doc)));
	}
	
	private void mapDocsIds() {
		String csvFile = "/Users/larissaleite/Downloads/ir-docs/docIds.csv";
		
		BufferedReader br = null;
		
		String line = null;

		HashMap<String, Integer> docIds = new HashMap<String, Integer>();
		
		try {
			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");
				docIds.put(row[1], Integer.parseInt(row[0]));
			}
			
			setDocIds(docIds);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public List<String> getSimilarItems() {
		return similarItems;
	}

	public void setSimilarItems(List<String> similarItems) {
		this.similarItems = similarItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public HashMap<String, Integer> getDocIds() {
		return docIds;
	}

	public void setDocIds(HashMap<String, Integer> docIds) {
		this.docIds = docIds;
	}

}
