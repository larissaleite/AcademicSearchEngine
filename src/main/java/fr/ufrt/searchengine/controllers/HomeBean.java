package fr.ufrt.searchengine.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ViewScoped;

import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Controller;

import fr.ufrt.searchengine.models.User;
import fr.ufrt.searchengine.recommender.KeywordRecommender;
import fr.ufrt.searchengine.voldemort.VoldemortDB;

@Controller
//@Scope("request")
@ViewScoped
public class HomeBean {
	
	private User user;

	private List<String> documents;
	private List<String> keywords;
	
	private List<String> keywordRecommendations;
	private List<String> documentsRecommendations;
	
	private KeywordRecommender keywordRecommeder;
	
	private HashMap<String, String> docIds;
	private HashMap<String, String> docNamesTitles;
	private HashMap<String, List<String>> documentsKeywords;
	
	public HomeBean() {
		this.keywordRecommeder = new KeywordRecommender();
		
		this.mapDocumentsKeywords();
	}
	
	public void mapRecommendedDocsIds() {
		String csvFile = "/Users/larissaleite/Downloads/users_stage3/docIds.csv";
		
		BufferedReader br = null;
		
		String line = null;

		docIds = new HashMap<String, String>();
		
		try {
			br = new BufferedReader(new FileReader(csvFile));

			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");
				if (documentsRecommendations.contains(row[1])) {
					docIds.put(row[0], row[1]);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mapDocsNameTitle() {
		String csvFile = "/Users/larissaleite/Downloads/users_stage3/paper.csv";
		
		docNamesTitles = new HashMap<String, String>();

		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(csvFile));

			//ignore reader
			String line = br.readLine();

			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");
				docNamesTitles.put(row[0], row[1]);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTopDocuments() {
		documents = new ArrayList<String>();
		
		if (isNewUser(getUser())) {
		
			BufferedReader br = null;
			
			try {
				br = new BufferedReader(new FileReader(
						"/Users/larissaleite/Downloads/users_stage3/top10docs.csv"));
				
				//ignore first line with header
				String line = br.readLine();
				
				while ((line = br.readLine()) != null) {
					String[] lineArray = line.split(",");
					String docName = lineArray[1];
					
					documents.add(docName);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			for (int i=0; i<10; i++) {
				documents.add(docNamesTitles.get(documentsRecommendations.get(i)));
			}
		}
	}
	
	public void setTopKeywords() {
		keywords = new ArrayList<String>();

		if (isNewUser(getUser())) {
			
			BufferedReader br = null;
			
			try {
				br = new BufferedReader(new FileReader(
						"/Users/larissaleite/Downloads/users_stage3/top10keywords.csv"));
				
				//ignore first line with header
				String line = br.readLine();
				
				while ((line = br.readLine()) != null) {
					String[] lineArray = line.split(",");
					String keyword = lineArray[0];
					
					keywords.add(WordUtils.capitalize(keyword));
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			keywords = this.keywordRecommeder.getKeywordRecommendation(new ArrayList<String>(docIds.keySet()), documentsKeywords, null, 10);
		}
	}
	
	private void mapDocumentsKeywords() {
		documentsKeywords = new HashMap<String, List<String>>();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(
					"/Users/larissaleite/Downloads/users_stage3/paper_keyword.csv"));
			
			//ignore first line with header
			String line = br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] lineArray = line.split(",");
				String docId = lineArray[0];
				
				if (documentsKeywords.get(docId) == null) {
					List<String> keywords = new ArrayList<String>();
					documentsKeywords.put(docId, keywords);
				}
				
				String keyword = lineArray[1];
				if (!documentsKeywords.get(docId).contains(keyword)) {
					documentsKeywords.get(docId).add(keyword);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isNewUser(User user) {
		return VoldemortDB.isNewUser(String.valueOf(user.getId()));
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public List<String> getDocuments() {
		return documents;
	}

	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}

	public List<String> getDocumentsRecommendations() {
		return documentsRecommendations;
	}

	public void setDocumentsRecommendations(List<String> documentsRecommendations) {
		this.documentsRecommendations = documentsRecommendations;
	}

	public List<String> getKeywordRecommendations() {
		return keywordRecommendations;
	}

	public void setKeywordRecommendations(List<String> keywordRecommendations) {
		this.keywordRecommendations = keywordRecommendations;
	}
}
