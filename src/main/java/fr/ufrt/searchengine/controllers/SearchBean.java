package fr.ufrt.searchengine.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.daos.interfaces.IInteractionDAO;
import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.Conference;
import fr.ufrt.searchengine.models.Interaction;
import fr.ufrt.searchengine.models.Item;
import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.recommender.Recommender;
import fr.ufrt.searchengine.recommender.content.ItemItemRecommender;
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

	private List<String> keywordRecommendations;

	private List<String> documentsRecommendations;
	
	private List<String> similarItems;
	
	private HashMap<String, Integer> docIds;
	private HashMap<Integer, Float> conferencesPreferences;
	private HashMap<String, Float> authorsPreferences;
	private HashMap<String, List<String>> documentKeywords;
	
	public SearchBean() {
		solrSearcher = new SolrSearcher();

		mapDocsIds();
		mapDocumentsKeywords();
	}

	private void mapDocumentsKeywords() {
		documentKeywords = new HashMap<String, List<String>>();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/paper_keyword.csv"));
			
			//ignore first line with header
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(",");
				String docId = lineArray[0];
				
				if (documentKeywords.get(docId) == null) {
					List<String> keywords = new ArrayList<String>();
					documentKeywords.put(docId, keywords);
				}
				
				String keyword = lineArray[1];
				documentKeywords.get(docId).add(keyword);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mapConferencesWeight() {
		String csvFile = "/Users/larissaleite/Downloads/users_stage3/user_conference_weight.csv";
		
		BufferedReader br = null;
		
		String line = null;

		conferencesPreferences = new HashMap<Integer, Float>();
		
		try {
			br = new BufferedReader(new FileReader(csvFile));

			//ignore header
			line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");
				if (getUser().getId() == Integer.parseInt(row[0])) {
					conferencesPreferences.put(Integer.parseInt(row[1]), Float.parseFloat(row[2]));
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mapAuthorsWeight() {
		String csvFile = "/Users/larissaleite/Downloads/users_stage3/user_author_weight.csv";
		
		BufferedReader br = null;
		
		String line = null;

		authorsPreferences = new HashMap<String, Float>();
		
		try {
			br = new BufferedReader(new FileReader(csvFile));

			//ignore header
			line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");
				if (getUser().getId() == Integer.parseInt(row[0])) {
					authorsPreferences.put(row[1], Float.parseFloat(row[2]));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void search() {
		List<Item> results = solrSearcher.search(query);

		List<Author> authors = getUser().getPreferredAuthors();
		List<Conference> conferences = getUser().getPreferredConferences();

		for (Item item : results) {
			//checkIfItemIsRecommended(item);
			checkIfAuthorIsPreferred(authors, item);
			checkIfConferenceIsPreferred(conferences, item);
		}
		
		sortResultsByFinalScore(results);
		setItems(results);

		setKeywordRecommendation();
	}
	
	@SuppressWarnings("rawtypes")
	private void setKeywordRecommendation() {
		HashMap<String, Boolean> keywords = new HashMap<String, Boolean>();
		this.keywordRecommendations = new ArrayList<String>();
		
		for (Item item : items) {
			if (item.getId() != null && documentKeywords.get(item.getId().get(0)) != null) {
				List<String> docKeywords = documentKeywords.get(item.getId().get(0));
				for (String keyword : docKeywords) {
					if (keywords.size() < 5) {
						keywords.put(keyword, true);
					}
					break;
				}
				if (keywords.size() == 5) {
					break;
				}
			}
		}
		
		Iterator it = keywords.keySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey().toString());
			this.keywordRecommendations.add(pair.getKey().toString());
		}
	}

	private void sortResultsByFinalScore(List<Item> results) {
		// Sorting to put highest scores first on the list
		Collections.sort(results, new Comparator<Item>() {
			@Override
			public int compare(Item doc1, Item doc2) {
				return Float.compare(doc2.getScore(),
						doc1.getScore());
			}
		});
	}

	private void checkIfAuthorIsPreferred(List<Author> authors, Item item) {
		if (item.getAuthors() != null) {
			for (Author author : authors) {
				if (item.getAuthors().get(0).contains(author.getName())) {
					item.setAuthorPreferred(true);
					
					float score = item.getScore();
					float weight = authorsPreferences.get(author.getAuthor_rec_id());
					
					item.setScore(score*(1+weight));
				}
			}
		}
	}
	
	private void checkIfConferenceIsPreferred(List<Conference> conferences, Item item) {
		if (item.getConference() != null) {
			for (Conference conference : conferences) {
				if (item.getConference().equals(conference)) {
					item.setConferencePreferred(true);
					
					float score = item.getScore();
					float weight = conferencesPreferences.get(conference.getId());
					
					item.setScore(score*(1+weight));
					break;
				}
			}
		}
	}

	private void checkIfItemIsRecommended(Item item) {
//		String[] idSplit = item.getId().get(0);.split("/");
//		String id = idSplit[idSplit.length - 1];
//
//		if (documentsRecommendations.contains(id)) {
//			item.setRecommended(true);
//		}
	}

	public void saveInteraction(Item item) {
		Long id = item.getId().get(0);
		item.setShowRating(true);
		
		/*Interaction interaction = new Interaction();
		interaction.setDocument(id);
		interaction.setUser(getUser());

		interactionDAO.register(interaction);
		 */
		// item item similarity
		Recommender itemRecommender = new ItemItemRecommender();
		//String[] idSplit = id.split("/");
		//String doc = idSplit[idSplit.length - 1];
		//setSimilarItems(itemRecommender.getRecommendations(new ArrayList<String>(), docIds.get(doc)));
		setSimilarItems(itemRecommender.getRecommendations(new ArrayList<String>(), (int) (long) id));
	}
	
	public void saveFeedback(Item item) {
		System.out.println("item feedback "+item.getRating());
		item.setShowRating(false);
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
		return keywordRecommendations;
	}

	public void setClusterRecommendations(List<String> clusterRecommendations) {
		this.keywordRecommendations = clusterRecommendations;
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
