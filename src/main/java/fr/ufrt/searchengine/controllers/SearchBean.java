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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RateEvent;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.models.Author;
import fr.ufrt.searchengine.models.Conference;
import fr.ufrt.searchengine.models.Item;
import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.recommender.KeywordRecommender;
import fr.ufrt.searchengine.recommender.Recommender;
import fr.ufrt.searchengine.recommender.collaborative.ItemItemRecommender;
import fr.ufrt.searchengine.searcher.SolrSearcher;
import fr.ufrt.searchengine.voldemort.VoldemortDB;

@Controller
@ViewScoped
public class SearchBean {

	private String query;
	private List<Item> items;
	private SolrSearcher solrSearcher;

	private User user;

	private List<String> keywordRecommendations;
	private List<String> documentsRecommendations;
	private List<String> similarItems;

	private HashMap<Integer, String> docIds;
	private HashMap<String, Float> conferencesPreferences;
	private HashMap<String, Float> authorsPreferences;
	private HashMap<String, List<String>> documentKeywords;

	private VoldemortDB voldemortDB;

	private KeywordRecommender keywordRecommeder;

	public SearchBean() {
		solrSearcher = new SolrSearcher();

		this.voldemortDB = new VoldemortDB();

		this.keywordRecommeder = new KeywordRecommender();

		mapDocsIds();
		mapDocumentsKeywords();
	}

	private void mapDocumentsKeywords() {
		documentKeywords = new HashMap<String, List<String>>();

		BufferedReader br = null;

		try {
			br = new BufferedReader(
					new FileReader(
							"/Users/larissaleite/Downloads/users_stage3/paper_keyword.csv"));

			// ignore first line with header
			String line = br.readLine();

			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(",");
				String docId = lineArray[0];

				if (documentKeywords.get(docId) == null) {
					List<String> keywords = new ArrayList<String>();
					documentKeywords.put(docId, keywords);
				}

				String keyword = lineArray[1];
				if (!documentKeywords.get(docId).contains(keyword)) {
					documentKeywords.get(docId).add(keyword);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void mapConferencesWeight() {
		String csvFile = "/Users/larissaleite/Downloads/users_stage3/user_conference_weight.csv";

		BufferedReader br = null;

		String line = null;

		conferencesPreferences = new LinkedHashMap<String, Float>();

		try {
			br = new BufferedReader(new FileReader(csvFile));

			// ignore header
			line = br.readLine();

			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");
				if (getUser().getId() == Integer.parseInt(row[0])) {
					conferencesPreferences.put(row[1],
							Float.parseFloat(row[2]));
				}
			}
			
			HashMap<String, Float> conferencesPreferencesSorted = sortByComparator(conferencesPreferences);
			
			this.conferencesPreferences.clear();
			
			int top5 = 0;
			
			Iterator it = conferencesPreferencesSorted.entrySet().iterator();
			while (it.hasNext() && top5 < 5) {
				Map.Entry pair = (Map.Entry) it.next();
				conferencesPreferences.put(pair.getKey().toString(), Float.parseFloat(pair.getValue().toString()));
				top5++;
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

			// ignore header
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
			checkIfItemIsRecommended(item);
			checkIfAuthorIsPreferred(authors, item);
			checkIfConferenceIsPreferred(conferences, item);
		}

		sortResultsByFinalScore(results);
		setItems(results);

		setKeywordRecommendation();
	}

	private void setKeywordRecommendation() {
		List<String> documentIds = new ArrayList<String>();

		for (Item item : items) {
			String itemID = String.valueOf(item.getId().get(0));
			if (itemID != null) {
				documentIds.add(itemID);
			}
		}

		this.keywordRecommendations = this.keywordRecommeder
				.getKeywordRecommendation(documentIds, documentKeywords, query,
						5);
	}

	private void sortResultsByFinalScore(List<Item> results) {
		// Sorting to put highest scores first on the list
		Collections.sort(results, new Comparator<Item>() {
			@Override
			public int compare(Item doc1, Item doc2) {
				return Float.compare(doc2.getScore(), doc1.getScore());
			}
		});
	}

	private void checkIfAuthorIsPreferred(List<Author> authors, Item item) {
		if (item.getAuthors() != null) {
			for (Author author : authors) {
				String authorsNames = item.getAuthors().get(0);
				if (authorsNames.contains(author.getName())) {
					item.setAuthorPreferred(true);

					float score = item.getScore();
					float weight = authorsPreferences.get(author
							.getAuthor_rec_id());

					item.setScore(score * (1 + weight));
				}
				String cleanedAuthorsNames = cleanAuthorsNames(authorsNames);
				item.getAuthors().set(0, cleanedAuthorsNames);
			}
		}
	}

	private String cleanAuthorsNames(String authorsNames) {
		authorsNames = authorsNames.replace(";", "; ");
		authorsNames = authorsNames.replace(",", "");
		return authorsNames;
	}

	private void checkIfConferenceIsPreferred(List<Conference> conferences,
			Item item) {
		if (item.getConference() != null) {
			for (Conference conference : conferences) {
				if (item.getConference().get(0).equals(conference.getName())) {

					float score = item.getScore();
					
					//fix because this is necessary for the top 3
					if (conferencesPreferences.get(String.valueOf(conference
							.getId())) != null) {
						float weight = conferencesPreferences.get(String.valueOf(conference
								.getId()));
						item.setConferencePreferred(true);
						item.setScore(score * (1 + weight));
					}
					break;
				}
			}
		}
	}

	private void checkIfItemIsRecommended(Item item) {
		String docName = docIds.get((int) (long) item.getId().get(0));
		if (documentsRecommendations.contains(docName)) {
			item.setRecommended(true);
		}
	}

	public void saveInteraction(Item item) {
		Long id = item.getId().get(0);
		item.setShowRating(true);

		this.voldemortDB.addUserDoc(String.valueOf(user.getId()) + "_" + id);

		// item item similarity
		Recommender itemRecommender = new ItemItemRecommender();
		setSimilarItems(itemRecommender.getRecommendations(
				new ArrayList<String>(), (int) (long) id));
	}

	public void saveFeedback(RateEvent rateEvent) {
		System.out.println("rate "+rateEvent.getRating());
		int rating = (int) rateEvent.getRating();
		String itemId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("item");
		itemId = itemId.substring(itemId.indexOf("[")+1,itemId.indexOf("]"));
		
		Item item = findItemById(Long.parseLong(itemId));
		if (item != null) {
			item.setShowRating(false);
			item.setRating(rating);
		}
	}
	
	private Item findItemById(Long id) {
		for (Item item : items) {
			if (item.getId().get(0).equals(id)) {
				return item;
			}
		}
		return null;
	}

	private HashMap<String, Float> sortByComparator(
			HashMap<String, Float> unsortMap) {

		List<Entry<String, Float>> list = new LinkedList<Entry<String, Float>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Float>>() {
			public int compare(Entry<String, Float> o1,
					Entry<String, Float> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		// Maintaining insertion order with the help of LinkedList
		HashMap<String, Float> sortedMap = new LinkedHashMap<String, Float>();
		for (Entry<String, Float> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private void mapDocsIds() {
		String csvFile = "/Users/larissaleite/Downloads/users_stage3/docIds.csv";

		BufferedReader br = null;

		String line = null;

		HashMap<Integer, String> docIds = new HashMap<Integer, String>();

		try {
			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");
				docIds.put(Integer.parseInt(row[0]), row[1]);
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

	public HashMap<Integer, String> getDocIds() {
		return docIds;
	}

	public void setDocIds(HashMap<Integer, String> docIds) {
		this.docIds = docIds;
	}

}
